package my_mall.consumer;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import my_mall.config.RabbitMQConfig;
import my_mall.constant.JudgeConstant;
import my_mall.constant.MessageConstant;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

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
    @Transactional(rollbackFor = Exception.class)
    public void handleSeckillRequest(SeckillMessage message) {
        Long userId = message.getUserId();
        Long seckillGoodsId = message.getSeckillGoodsId();
        String stockKey = "seckill:stock:" + seckillGoodsId;
        String userKey = "seckill:user:" + seckillGoodsId + ":" + userId;
        String failKey = "seckill:fail:" + seckillGoodsId + ":" + userId;

        log.info("处理秒杀请求: userId={}, seckillGoodsId={}", userId, seckillGoodsId);

        // 分布式锁：同一秒杀商品同一时刻只有一个请求能扣库存，防止超卖
        RLock lock = redissonClient.getLock("seckill:lock:" + seckillGoodsId);
        boolean locked = false;
        try {
            try {
                locked = lock.tryLock(3, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.warn("获取秒杀锁被中断: userId={}, seckillGoodsId={}", userId, seckillGoodsId);
                redisTemplate.opsForValue().set(failKey, "FAIL", Duration.ofHours(2));
                return;
            }
            if (!locked) {
                log.warn("获取秒杀锁失败: userId={}, seckillGoodsId={}", userId, seckillGoodsId);
                redisTemplate.opsForValue().set(failKey, "FAIL", Duration.ofHours(2));
                return;
            }

            // 查库存
            String stockStr = redisTemplate.opsForValue().get(stockKey);
            if (stockStr == null || Integer.parseInt(stockStr) < message.getCount()) {
                log.warn("库存不足: userId={}, seckillGoodsId={}", userId, seckillGoodsId);
                redisTemplate.opsForValue().set(failKey, "FAIL", Duration.ofHours(2));
                return;
            }

            // 查重复
            if (Boolean.TRUE.equals(redisTemplate.hasKey(userKey))) {
                log.warn("重复秒杀: userId={}, seckillGoodsId={}", userId, seckillGoodsId);
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

        // 锁外：写秒杀订单（数据库操作放锁外，减少锁持有时间）
        try {
            var seckillGoods = seckillGoodsMapper.getById(seckillGoodsId);
            if (seckillGoods == null) {
                throw new SeckillException(MessageConstant.SECKILL_GOODS_NOT_EXIST);
            }
            //数据库层再次去重（Redis 的 userKey 有 TTL，过期后靠数据库兜底）
            SeckillOrder existing = seckillOrderMapper.getByUserIdAndGoodsId(userId, seckillGoods.getGoodsId());
            if (existing != null) {
                redisTemplate.opsForValue().increment(stockKey, message.getCount());
                redisTemplate.delete(userKey);
                return;
            }

            //秒杀时只扣 Redis，数据库库存由 SeckillOrderConsumer 异步落库
            SeckillOrder seckillOrder = SeckillOrder.builder()
                    .userId(userId)
                    .goodsId(seckillGoods.getGoodsId())
                    .orderId(0L)
                    .status(JudgeConstant.ENABLE)
                    .build();
            seckillOrderMapper.insert(seckillOrder);

            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    rabbitTemplate.convertAndSend(RabbitMQConfig.SECKILL_QUEUE, message);
                    log.info("秒杀请求处理成功: userId={}, seckillGoodsId={}", userId, seckillGoodsId);
                }
            });
        } catch (Exception e) {
            log.error("秒杀处理异常: userId={}, seckillGoodsId={}", userId, seckillGoodsId, e);
            redisTemplate.opsForValue().increment(stockKey, message.getCount());
            redisTemplate.delete(userKey);
            redisTemplate.opsForValue().set(failKey, "FAIL", Duration.ofHours(2));
        }
    }
}
