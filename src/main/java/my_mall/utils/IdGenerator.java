package my_mall.utils;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Component
public class IdGenerator {

    private final Snowflake snowflake;

    public IdGenerator() {
        //根据 hostname 计算 workerId，保证多实例下各实例的 workerId 不同，避免生成重复订单号
        this.snowflake = IdUtil.getSnowflake(getWorkerId(), 1L);
    }

    public String generateOrderNo() {
        return snowflake.nextIdStr();
    }

    private long getWorkerId() {
        try {
            String hostname = InetAddress.getLocalHost().getHostName();
            return Math.abs((long) hostname.hashCode()) % 32;
        } catch (UnknownHostException e) {
            return 1L;
        }
    }
}
