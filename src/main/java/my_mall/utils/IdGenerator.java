package my_mall.utils;

import cn.hutool.core.util.IdUtil;
import org.springframework.stereotype.Component;

@Component
public class IdGenerator {

    public String generateOrderNo() {
        return IdUtil.getSnowflake().nextIdStr();
    }
}
