package my_mall.config;


import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import my_mall.entity.properties.OssProperties;
import my_mall.utils.OssUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OssConfig {

    @Bean
    public OssUtils aliOssUtil(OssProperties aliOssProperties, OSS ossClient) {
        return new OssUtils(aliOssProperties.getEndpoint(), aliOssProperties.getBucketName(), ossClient);
    }
    @Bean(destroyMethod = "shutdown")
    public OSS ossClient(OssProperties aliOssProperties) {
        return new OSSClientBuilder().build(
                aliOssProperties.getEndpoint(),
                aliOssProperties.getAccessKeyId(),
                aliOssProperties.getAccessKeySecret());
    }
}
