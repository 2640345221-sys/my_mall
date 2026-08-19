package my_mall.consumer;

import lombok.extern.slf4j.Slf4j;
import my_mall.config.RabbitMQConfig;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class SeckillDeadConsumer {
    //监听死信队列，记录秒杀处理失败的消息，方便排查
    @RabbitListener(queues = RabbitMQConfig.SECKILL_DEAD_QUEUE)
    public void handleDeadMessage(Message message) {
        String body = new String(message.getBody(), StandardCharsets.UTF_8);
        log.error("秒杀死信消息（处理失败）: {}", body);
    }
}
