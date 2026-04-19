package my_mall.config;


import lombok.extern.slf4j.Slf4j;
import my_mall.entity.properties.OssProperties;
import my_mall.utils.OssUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class OssConfig {

    @Bean
    public OssUtils aliOssUtil(OssProperties aliOssProperties) {
        log.info("开始加载阿里云OSS");
        return new OssUtils(aliOssProperties.getEndpoint(), aliOssProperties.getAccessKeyId()
                , aliOssProperties.getAccessKeySecret(), aliOssProperties.getBucketName() );
    }
}
