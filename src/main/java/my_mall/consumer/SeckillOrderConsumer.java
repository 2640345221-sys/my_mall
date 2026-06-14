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
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
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

    @RabbitListener(queues = RabbitMQConfig.SECKILL_QUEUE)
    @Transactional(rollbackFor = Exception.class)
    public void handleSeckillOrder(SeckillMessage message) {
        log.info("收到秒杀订单消息: userId={}, seckillGoodsId={}", message.getUserId(), message.getSeckillGoodsId());

        UserAddress address = addressMapper.getAddressById(message.getAddressId());
        if (address == null || !address.getUserId().equals(message.getUserId())) {
            throw new AddressNotExistException(MessageConstant.ADDRESS_NOT_EXIST);
        }

        SeckillGoods seckillGoods = seckillGoodsMapper.getById(message.getSeckillGoodsId());
        if (seckillGoods == null) {
            throw new SeckillException(MessageConstant.SECKILL_GOODS_NOT_EXIST);
        }
        Goods goods = goodsMapper.getById(seckillGoods.getGoodsId());

        Order order = Order.builder()
                .userId(message.getUserId())
                .totalPrice(seckillGoods.getSeckillPrice() * message.getCount())
                .orderNo(idGenerator.generateOrderNo())
                .extraInfo("")
                .payType(OrderPayTypeEnum.NO_PAY.getValue())
                .payStatus(OrderPayStatusEnum.NO_PAY.getValue())
                .orderStatus(OrderStatusEnum.ORDER_PRE_PAY.getStatus())
                .build();
        orderMapper.insert(order);

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

        log.info("秒杀订单写入完成: orderId={}", order.getId());
    }
}
