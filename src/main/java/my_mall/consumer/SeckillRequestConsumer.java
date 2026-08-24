package my_mall.consumer;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import my_mall.config.RabbitMQConfig;
import my_mall.entity.dto.SeckillMessage;
import my_mall.exception.SeckillException;
import my_mall.service.SeckillGoodsService;
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
    private SeckillGoodsService seckillGoodsService;

    @RabbitListener(queues = RabbitMQConfig.SECKILL_REQUEST_QUEUE)
    public void handleSeckillRequest(SeckillMessage message) {
        Long userId = message.getUserId();
        Long seckillGoodsId = message.getSeckillGoodsId();
        String stockKey = "seckill:stock:" + seckillGoodsId;
        String userKey = "seckill:user:" + seckillGoodsId + ":" + userId;
        String failKey = "seckill:fail:" + seckillGoodsId + ":" + userId;

        //新的秒杀请求先清掉上次的失败标记，否则结果查询会一直返回失败
        redisTemplate.delete(failKey);

        // 分布式锁：同一秒杀商品同一时刻只有一个请求能扣库存，防止超卖
        RLock lock = redissonClient.getLock("seckill:lock:" + seckillGoodsId);
        boolean locked = false;
        try {
            try {
                locked = lock.tryLock(3, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                redisTemplate.opsForValue().set(failKey, "FAIL", Duration.ofHours(2)); //设置为失败，让用户再次尝试秒杀
                return;
            }
            if (!locked) {
                log.warn("获取秒杀锁失败: userId={}, seckillGoodsId={}", userId, seckillGoodsId);
                redisTemplate.opsForValue().set(failKey, "FAIL", Duration.ofHours(2));
                return;
            }

            // 查库存；key 因 TTL 过期缺失时从数据库兜底预热，避免长期活动中途全部失败
            String stockStr = redisTemplate.opsForValue().get(stockKey);
            if (stockStr == null) {
                try {
                    var goods = seckillGoodsService.getById(seckillGoodsId);
                    stockStr = String.valueOf(goods.getStockCount().intValue());
                } catch (SeckillException e) {
                    redisTemplate.opsForValue().set(failKey, "FAIL", Duration.ofHours(2));
                    return;
                }
                redisTemplate.opsForValue().set(stockKey, stockStr, Duration.ofHours(2));
            }
            //库存不够了
            if (Integer.parseInt(stockStr) < message.getCount()) {
                redisTemplate.opsForValue().set(failKey, "FAIL", Duration.ofHours(2));
                return;
            }

            // 查重复
            if (Boolean.TRUE.equals(redisTemplate.hasKey(userKey))) {
                return;
            }

            // 扣库存 + 标记用户已秒杀
            redisTemplate.opsForValue().decrement(stockKey, message.getCount());
            redisTemplate.opsForValue().set(userKey, "1", Duration.ofHours(1));
        } finally {
            if (locked && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }

        // 锁外只转发，DB 落库和失败回补由 SeckillOrderConsumer 收到 SECKILL_QUEUE 后异步完成
        rabbitTemplate.convertAndSend(RabbitMQConfig.SECKILL_QUEUE, message);
    }
}
