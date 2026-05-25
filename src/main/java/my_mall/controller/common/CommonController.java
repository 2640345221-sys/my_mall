package my_mall.controller.common;

import java.io.IOException;
import java.util.UUID;

import jakarta.annotation.Resource;
import my_mall.exception.UploadFileFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import my_mall.annotation.OperationLog;
import my_mall.result.Result;
import my_mall.utils.OssUtils;

@Tag(name = "通用接口模块")
@RestController
@RequestMapping("/api/admin/common")
@Slf4j
public class CommonController {
    @Resource
    private OssUtils aliOssUtil;
    @Operation(summary = "文件上传")
    @OperationLog(module = "通用接口模块", type = "上传", description = "文件上传",
            recordParams = true, recordResult = true)
    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file){
        if (file.isEmpty()) {
            throw new UploadFileFailedException("文件为空");
        }
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.isEmpty()) {
            throw new UploadFileFailedException("文件名为空");
        }

        int dotIndex = originalFilename.lastIndexOf(".");
        if (dotIndex <= 0) {
            throw new UploadFileFailedException("无法识别文件扩展名");
        }

        String extension = originalFilename.substring(dotIndex);

        try {
            byte[] bytes = file.getBytes();
            String fileName = UUID.randomUUID().toString() + extension;
            String filePath = aliOssUtil.upload(bytes, fileName);
            return Result.success(filePath);
        } catch (IOException e) {
            throw new UploadFileFailedException(e.getMessage());
        }
    }
}
