package my_mall.consumer;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import my_mall.config.RabbitMQConfig;
import my_mall.entity.dto.SeckillMessage;
import my_mall.mapper.SeckillOrderMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@Slf4j
//监听死信队列，秒杀落库失败的消息进这里，做补偿：回补Redis库存、放行用户重试、通知前端失败
public class SeckillDeadConsumer {

    @Resource(name = "stringRedisTemplate")
    private StringRedisTemplate redisTemplate;
    @Resource
    private SeckillOrderMapper seckillOrderMapper;

    @RabbitListener(queues = RabbitMQConfig.SECKILL_DEAD_QUEUE)
    public void handleDeadMessage(SeckillMessage message) {
        Long userId = message.getUserId();
        Long seckillGoodsId = message.getSeckillGoodsId();
        Integer count = message.getCount();
        String stockKey = "seckill:stock:" + seckillGoodsId;
        String userKey = "seckill:user:" + seckillGoodsId + ":" + userId;
        String failKey = "seckill:fail:" + seckillGoodsId + ":" + userId;

        //幂等闸：删除未落库的秒杀订单（order_id=0）。删到0行说明已回补过，跳过，防止死信重发重复回补
        int rows = seckillOrderMapper.deletePending(userId, seckillGoodsId);
        if (rows == 0) {
            log.info("秒杀死信已回补过，跳过: userId={}, seckillGoodsId={}", userId, seckillGoodsId);
            return;
        }
        //回补Redis库存，清掉去重标记让用户能重试，打失败标记让前端轮询返回失败
        redisTemplate.opsForValue().increment(stockKey, count);
        redisTemplate.delete(userKey);
        redisTemplate.opsForValue().set(failKey, "FAIL", Duration.ofHours(2));
        log.warn("秒杀落库失败，死信回补完成: userId={}, seckillGoodsId={}, count={}", userId, seckillGoodsId, count);
    }
}
