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
import my_mall.constant.MessageConstant;
import my_mall.annotation.OperationLog;
import my_mall.result.Result;
import my_mall.utils.OssUtils;

@Tag(name = "通用接口模块")
@RestController
@RequestMapping("/api/admin/common")
@Slf4j
//通用接口：文件上传到 OSS
public class CommonController {
    @Resource
    private OssUtils aliOssUtil;
    @Operation(summary = "文件上传")
    @OperationLog(module = "通用接口模块", type = "上传", description = "文件上传",
            recordParams = true, recordResult = true)
    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file){
        if (file.isEmpty()) {
            throw new UploadFileFailedException(MessageConstant.FILE_EMPTY);
        }
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.isEmpty()) {
            throw new UploadFileFailedException(MessageConstant.FILE_NAME_EMPTY);
        }

        int dotIndex = originalFilename.lastIndexOf(".");
        if (dotIndex <= 0) {
            throw new UploadFileFailedException(MessageConstant.FILE_EXTENSION_UNRECOGNIZED);
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
