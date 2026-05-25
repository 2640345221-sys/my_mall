package my_mall.consumer;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import my_mall.config.RabbitMQConfig;
import my_mall.constant.JudgeConstant;
import my_mall.entity.dto.SeckillMessage;
import my_mall.entity.po.SeckillOrder;
import my_mall.exception.SeckillException;
import my_mall.mapper.SeckillGoodsMapper;
import my_mall.mapper.SeckillOrderMapper;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class SeckillRequestConsumer {

    @Resource(name = "stringRedisTemplate")
    private StringRedisTemplate redisTemplate;
    @Resource
    private RedissonClient redissonClient;
    @Resource
    private RabbitTemplate rabbitTemplate;
    @Resource
    private SeckillGoodsMapper seckillGoodsMapper;
    @Resource
    private SeckillOrderMapper seckillOrderMapper;

    @RabbitListener(queues = RabbitMQConfig.SECKILL_REQUEST_QUEUE)
    public void handleSeckillRequest(SeckillMessage message) {
        Long userId = message.getUserId();
        Long seckillGoodsId = message.getSeckillGoodsId();
        String stockKey = "seckill:stock:" + seckillGoodsId;
        String userKey = "seckill:user:" + seckillGoodsId + ":" + userId;

        log.info("处理秒杀请求: userId={}, seckillGoodsId={}", userId, seckillGoodsId);

        String stockStr = redisTemplate.opsForValue().get(stockKey);
        if (stockStr == null || Integer.parseInt(stockStr) <= 0) {
            log.warn("库存不足: userId={}, seckillGoodsId={}", userId, seckillGoodsId);
            return;
        }
        if (Boolean.TRUE.equals(redisTemplate.hasKey(userKey))) {
            log.warn("重复秒杀: userId={}, seckillGoodsId={}", userId, seckillGoodsId);
            return;
        }

        RLock lock = redissonClient.getLock("seckill:lock:" + seckillGoodsId);
        boolean locked = false;
        try {
            locked = lock.tryLock(0, 10, TimeUnit.SECONDS);
            if (!locked) {
                log.warn("获取锁失败: userId={}, seckillGoodsId={}", userId, seckillGoodsId);
                return;
            }
            String currentStockStr = redisTemplate.opsForValue().get(stockKey);
            if (currentStockStr == null || Integer.parseInt(currentStockStr) <= 0) {
                log.warn("双重检查库存不足: userId={}, seckillGoodsId={}", userId, seckillGoodsId);
                return;
            }
            if (Boolean.TRUE.equals(redisTemplate.hasKey(userKey))) {
                log.warn("双重检查重复秒杀: userId={}, seckillGoodsId={}", userId, seckillGoodsId);
                return;
            }

            Long newStock = redisTemplate.opsForValue().decrement(stockKey, message.getCount());
            if (newStock < 0) {
                redisTemplate.opsForValue().increment(stockKey, message.getCount());
                log.warn("扣减库存不足: userId={}, seckillGoodsId={}", userId, seckillGoodsId);
                return;
            }
            redisTemplate.opsForValue().set(userKey, "1", Duration.ofHours(1));

            int rows = seckillGoodsMapper.decreaseStock(seckillGoodsId, message.getCount());
            if (rows == 0) {
                throw new SeckillException("库存不足");
            }
            Long goodsId = seckillGoodsMapper.getById(seckillGoodsId).getGoodsId();
            SeckillOrder seckillOrder = SeckillOrder.builder()
                    .userId(userId)
                    .goodsId(goodsId)
                    .orderId(0L)
                    .status(JudgeConstant.ENABLE)
                    .build();
            seckillOrderMapper.insert(seckillOrder);

            rabbitTemplate.convertAndSend(RabbitMQConfig.SECKILL_QUEUE, message);
            log.info("秒杀请求处理成功: userId={}, seckillGoodsId={}", userId, seckillGoodsId);
        } catch (Exception e) {
            log.error("秒杀处理异常: userId={}, seckillGoodsId={}", userId, seckillGoodsId, e);
            redisTemplate.opsForValue().increment(stockKey);
            redisTemplate.delete(userKey);
            redisTemplate.opsForValue().set("seckill:fail:" + seckillGoodsId + ":" + userId, "FAIL", Duration.ofHours(2));
        } finally {
            if (locked && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}
