package my_mall.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@Slf4j
public class RedisConfiguration {
    //使用配置好的Jackson，确保转换正确
    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory,
                                       GenericJackson2JsonRedisSerializer serializer) {
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(serializer);
        redisTemplate.setHashValueSerializer(serializer);

        return redisTemplate;
    }

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
