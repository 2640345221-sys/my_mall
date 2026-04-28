package my_mall;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MyTest {
    @Resource
    private StringRedisTemplate stringRedisTemplate;  // 注意类型

    @Test
    public void testStringSet() {
        stringRedisTemplate.opsForValue().set("myKey", "myValue");
        System.out.println("写入完成，请去 Redis 查看 key=myKey");
    }
}