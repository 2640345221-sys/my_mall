package my_mall.controller.common;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import my_mall.result.Result;
import my_mall.utils.OssUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Tag(name = "通用接口模块")
@RestController
@RequestMapping("/api/admin/common")
@Slf4j
public class CommonController {
    @Autowired
    private OssUtils aliOssUtil;

    @Operation(summary = "文件上传")
    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file){
        log.info("开始上传文件"+file.getOriginalFilename());
        try {
            String format = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            UUID uuid = UUID.randomUUID();

            String fileName = uuid.toString()+"."+format;

            byte[] bytes=file.getBytes();

            String filePath=aliOssUtil.upload(bytes,fileName);

            return Result.success(filePath);

        } catch (IOException e) {
            log.error("上传失败");
        }
        return Result.error("上传失败");
    }
}
