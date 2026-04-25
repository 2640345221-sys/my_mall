package my_mall.entity.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "kkek.jwt")
public class JwtProperties {
    /**
     * 管理员相关属性
     * @return
     */
    private String adminSecretKey;
    private Long adminTtl;
    private String adminTokenName;

    /**
     * 用户相关属性
     * @return
     */
    private String userSecretKey;
    private Long userTtl;
    private String userTokenName;
}
