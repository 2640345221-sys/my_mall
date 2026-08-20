package my_mall.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

@Configuration
public class RedisConfiguration {

    //缓存失效监听容器：订阅频道，收到消息后清本地缓存（跨实例失效）
    @Bean
    public RedisMessageListenerContainer cacheEvictContainer(RedisConnectionFactory redisConnectionFactory,
                                                             CacheEvictListener cacheEvictListener) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        container.addMessageListener(cacheEvictListener, new ChannelTopic(CacheEvictListener.CACHE_EVICT_TOPIC));
        return container;
    }
}
