package my_mall.utils;

import java.io.ByteArrayInputStream;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSException;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import my_mall.exception.BaseException;
import my_mall.exception.UploadFileFailedException;

@Data
@AllArgsConstructor
@Slf4j
public class OssUtils {

    private String endpoint;
    private String bucketName;
    private OSS ossClient;

    /**
     * 文件上传
     *
     * @param bytes
     * @param objectName
     * @return
     */
    public String upload(byte[] bytes, String objectName) {

        try {
            ossClient.putObject(bucketName, objectName, new ByteArrayInputStream(bytes));
        } catch (OSSException oe) {
            log.error("OSS异常: {}", oe.getErrorMessage());
            log.error("错误代码: {}, 请求ID: {}, Host ID: {}",
                oe.getErrorCode(), oe.getRequestId(), oe.getHostId());
            throw new UploadFileFailedException(oe.getMessage());
        } catch (ClientException ce) {
            log.error("客户端异常: {}", ce.getMessage());
            throw new BaseException(ce.getMessage());
        }

        StringBuilder stringBuilder = new StringBuilder("https://");
        stringBuilder
                .append(bucketName)
                .append(".")
                .append(endpoint)
                .append("/")
                .append(objectName);

        log.info("文件上传到:{}", stringBuilder.toString());

        return stringBuilder.toString();

    }
}
