package my_mall.consumer;

import lombok.extern.slf4j.Slf4j;
import my_mall.config.RabbitMQConfig;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DeadLetterConsumer {

    @RabbitListener(queues = RabbitMQConfig.DLQ)
    public void handleDeadLetter(Message message) {
        log.error("死信消息: body={}, properties={}",
                new String(message.getBody()), message.getMessageProperties());
    }
}
