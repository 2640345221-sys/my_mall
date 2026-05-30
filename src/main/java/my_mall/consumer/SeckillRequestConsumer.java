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
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;

@Component
@Slf4j
public class SeckillRequestConsumer {

    private static final DefaultRedisScript<Long> SECKILL_SCRIPT;

    static {
        SECKILL_SCRIPT = new DefaultRedisScript<>();
        SECKILL_SCRIPT.setLocation(new ClassPathResource("lua/seckill.lua"));
        SECKILL_SCRIPT.setResultType(Long.class);
    }

    @Resource(name = "stringRedisTemplate")
    private StringRedisTemplate redisTemplate;
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

        Long result = redisTemplate.execute(
                SECKILL_SCRIPT,
                List.of(stockKey, userKey),
                String.valueOf(message.getCount()),
                String.valueOf(Duration.ofHours(1).getSeconds())
        );

        if (result == null) {
            log.error("Lua 脚本执行异常: userId={}, seckillGoodsId={}", userId, seckillGoodsId);
            redisTemplate.opsForValue().set("seckill:fail:" + seckillGoodsId + ":" + userId, "FAIL", Duration.ofHours(2));
            return;
        }

        if (result == -1) {
            log.warn("库存不足: userId={}, seckillGoodsId={}", userId, seckillGoodsId);
            redisTemplate.opsForValue().set("seckill:fail:" + seckillGoodsId + ":" + userId, "FAIL", Duration.ofHours(2));
            return;
        }

        if (result == -2) {
            log.warn("重复秒杀: userId={}, seckillGoodsId={}", userId, seckillGoodsId);
            return;
        }

        try {
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
        }
    }
}
