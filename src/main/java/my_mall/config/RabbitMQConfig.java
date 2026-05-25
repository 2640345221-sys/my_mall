package my_mall.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class RabbitMQConfig {

    public static final String SECKILL_REQUEST_QUEUE = "seckill.request.queue";
    public static final String SECKILL_QUEUE = "seckill.order.queue";

    @Bean
    public Queue seckillRequestQueue() {
        return new Queue(SECKILL_REQUEST_QUEUE, true, false, false, Map.of("x-queue-mode", "lazy"));
    }

    @Bean
    public Queue seckillOrderQueue() {
        return new Queue(SECKILL_QUEUE, true, false, false, Map.of("x-queue-mode", "lazy"));
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         Jackson2JsonMessageConverter converter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(converter);
        return template;
    }
}
