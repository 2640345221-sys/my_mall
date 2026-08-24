package my_mall.consumer;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import my_mall.config.RabbitMQConfig;
import my_mall.constant.MessageConstant;
import my_mall.entity.dto.SeckillMessage;
import my_mall.entity.po.*;
import my_mall.enums.OrderPayStatusEnum;
import my_mall.enums.OrderPayTypeEnum;
import my_mall.enums.OrderStatusEnum;
import my_mall.exception.AddressNotExistException;
import my_mall.exception.SeckillException;
import my_mall.mapper.*;
import my_mall.utils.IdGenerator;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Component
@Slf4j
//收到 SECKILL_QUEUE 后异步落库：写秒杀订单占位 + 创建正式订单，失败则回补 Redis 预扣
public class SeckillOrderConsumer {

    @Resource
    private GoodsMapper goodsMapper;
    @Resource
    private SeckillGoodsMapper seckillGoodsMapper;
    @Resource
    private OrderMapper orderMapper;
    @Resource
    private OrderItemMapper orderItemMapper;
    @Resource
    private AddressMapper addressMapper;
    @Resource
    private OrderAddressMapper orderAddressMapper;
    @Resource
    private IdGenerator idGenerator;
    @Resource
    private SeckillOrderMapper seckillOrderMapper;
    @Resource(name = "stringRedisTemplate")
    private StringRedisTemplate redisTemplate;

    @RabbitListener(queues = RabbitMQConfig.SECKILL_QUEUE)
    @Transactional(rollbackFor = Exception.class)
    public void handleSeckillOrder(SeckillMessage message) {
        Long userId = message.getUserId();
        Long seckillGoodsId = message.getSeckillGoodsId();
        String stockKey = "seckill:stock:" + seckillGoodsId;
        String userKey = "seckill:user:" + seckillGoodsId + ":" + userId;
        String failKey = "seckill:fail:" + seckillGoodsId + ":" + userId;

        try {
            UserAddress address = addressMapper.getAddressById(message.getAddressId());
            if (address == null || !address.getUserId().equals(userId)) {
                throw new AddressNotExistException(MessageConstant.ADDRESS_NOT_EXIST);
            }

            SeckillGoods seckillGoods = seckillGoodsMapper.getById(seckillGoodsId);
            if (seckillGoods == null) {
                throw new SeckillException(MessageConstant.SECKILL_GOODS_NOT_EXIST);
            }
            Goods goods = goodsMapper.getById(seckillGoods.getGoodsId());
            if (goods == null) {
                throw new SeckillException(MessageConstant.GOODS_NOT_EXIST);
            }

            //数据库层去重（Redis 的 userKey 有 TTL，过期后靠数据库兜底）：已下单则回补本次预扣，不落库
            SeckillOrder existing = seckillOrderMapper.getByUserIdAndSeckillGoodsId(userId, seckillGoodsId);
            if (existing != null) {
                redisTemplate.opsForValue().increment(stockKey, message.getCount());
                redisTemplate.delete(userKey);
                return;
            }
            //秒杀订单占位（order_id=0），正式订单随后创建并回写关联
            SeckillOrder seckillOrder = SeckillOrder.builder()
                    .userId(userId)
                    .seckillGoodsId(seckillGoodsId)
                    .orderId(0L)
                    .status(OrderStatusEnum.ORDER_PRE_PAY.getStatus())
                    .build();
            seckillOrderMapper.insert(seckillOrder);

            Order order = Order.builder()
                    .userId(userId)
                    .totalPrice(seckillGoods.getSeckillPrice() * message.getCount())
                    .orderNo(idGenerator.generateOrderNo())
                    .extraInfo("")
                    .payType(OrderPayTypeEnum.NO_PAY.getValue())
                    .payStatus(OrderPayStatusEnum.NO_PAY.getValue())
                    .orderStatus(OrderStatusEnum.ORDER_PRE_PAY.getStatus())
                    .build();
            orderMapper.insert(order);

            //异步扣减数据库库存（秒杀时已扣 Redis，这里落库）
            int rows = seckillGoodsMapper.decreaseStock(seckillGoodsId, message.getCount());
            if (rows == 0) {
                throw new SeckillException(MessageConstant.SECKILL_STOCK_NOT_ENOUGH);
            }

            //回写秒杀订单的 orderId，关联普通订单
            SeckillOrder persisted = seckillOrderMapper.getByUserIdAndSeckillGoodsId(userId, seckillGoodsId);
            if (persisted != null) {
                seckillOrderMapper.updateOrderId(persisted.getId(), order.getId());
            }

            OrderItem orderItem = OrderItem.builder()
                    .orderId(order.getId())
                    .goodsId(goods.getId())
                    .goodsName(goods.getName())
                    .coverImg(goods.getCoverImg())
                    .price(seckillGoods.getSeckillPrice())
                    .count(message.getCount())
                    .build();
            orderItemMapper.insert(orderItem);

            OrderAddress orderAddress = new OrderAddress();
            orderAddress.setUsername(address.getUsername());
            orderAddress.setUserPhone(address.getUserPhone());
            orderAddress.setProvince(address.getProvince());
            orderAddress.setCity(address.getCity());
            orderAddress.setRegion(address.getRegion());
            orderAddress.setDetailAddress(address.getDetailAddress());
            orderAddress.setOrderId(order.getId());
            orderAddressMapper.insert(orderAddress);
        } catch (Exception e) {
            log.error("秒杀订单落库异常: userId={}, seckillGoodsId={}", userId, seckillGoodsId, e);
            //回补Redis库存、清去重标记、打失败标记；再抛出触发事务回滚（含秒杀订单占位）并进死信队列兜底
            redisTemplate.opsForValue().increment(stockKey, message.getCount());
            redisTemplate.delete(userKey);
            redisTemplate.opsForValue().set(failKey, "FAIL", Duration.ofHours(2));
            throw e;
        }
    }
}
