package my_mall.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class CaffeineCacheConfig {
    //两个缓存，只为最频繁访问的数据存储了，商品种类和 热门，最新，推荐商品
    //最新商品不是所有商品的最新，而是展示界面的最新，10分钟一刷新是可以的
    @Bean
    public Cache<String, Object> categoryCache() {
        return Caffeine.newBuilder()
                .maximumSize(500)
                .expireAfterWrite(30, TimeUnit.MINUTES)
                .build();
    }

    @Bean
    public Cache<String, Object> goodsCache() {
        return Caffeine.newBuilder()
                .maximumSize(2000)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .build();
    }
}
