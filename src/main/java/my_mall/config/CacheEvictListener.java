package my_mall.config;

import com.github.benmanes.caffeine.cache.Cache;
import jakarta.annotation.Resource;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

//缓存失效监听器：订阅 Redis 频道，收到通知后清本实例的本地缓存（解决多实例本地缓存不一致）
@Component
public class CacheEvictListener implements MessageListener {
    //缓存失效通知的频道名
    public static final String CACHE_EVICT_TOPIC = "cache:evict";
    //分类缓存标识
    public static final String CATEGORY_CACHE = "category";
    //首页配置缓存标识
    public static final String INDEX_CONFIG_CACHE = "indexConfig";

    @Resource
    private Cache<String, Object> categoryCache;
    @Resource
    private Cache<String, Object> goodsCache;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String cacheName = new String(message.getBody(), StandardCharsets.UTF_8);
        if (CATEGORY_CACHE.equals(cacheName)) {
            categoryCache.invalidateAll();
        } else if (INDEX_CONFIG_CACHE.equals(cacheName)) {
            goodsCache.invalidateAll();
        }
    }
}
