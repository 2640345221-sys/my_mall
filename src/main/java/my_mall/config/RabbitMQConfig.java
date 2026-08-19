package my_mall.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
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
    //死信交换机、死信队列、路由键（重试耗尽的消息进这里兜底）
    public static final String SECKILL_DEAD_EXCHANGE = "seckill.dead.exchange";
    public static final String SECKILL_DEAD_QUEUE = "seckill.dead.queue";
    public static final String SECKILL_DEAD_ROUTING_KEY = "seckill.dead";
    //两个队列，一个是处理用户发送请求秒杀，另一个是解耦发送到后端去进行数据库的修改
    @Bean
    public Queue seckillRequestQueue() {
        return new Queue(SECKILL_REQUEST_QUEUE, true, false, false, Map.of(
                "x-queue-mode", "lazy",
                "x-dead-letter-exchange", SECKILL_DEAD_EXCHANGE,
                "x-dead-letter-routing-key", SECKILL_DEAD_ROUTING_KEY
        ));
    }

    @Bean
    public Queue seckillOrderQueue() {
        return new Queue(SECKILL_QUEUE, true, false, false, Map.of(
                "x-queue-mode", "lazy",
                "x-dead-letter-exchange", SECKILL_DEAD_EXCHANGE,
                "x-dead-letter-routing-key", SECKILL_DEAD_ROUTING_KEY
        ));
    }

    //死信交换机
    @Bean
    public DirectExchange seckillDeadExchange() {
        return new DirectExchange(SECKILL_DEAD_EXCHANGE, true, false);
    }

    //死信队列
    @Bean
    public Queue seckillDeadQueue() {
        return new Queue(SECKILL_DEAD_QUEUE, true);
    }

    //死信队列绑定死信交换机
    @Bean
    public Binding seckillDeadBinding() {
        return BindingBuilder.bind(seckillDeadQueue()).to(seckillDeadExchange()).with(SECKILL_DEAD_ROUTING_KEY);
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
