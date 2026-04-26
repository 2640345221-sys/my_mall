package my_mall.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import my_mall.annotation.OperationLog;
import my_mall.entity.dto.IndexConfigDTO;
import my_mall.entity.dto.IndexPageDTO;
import my_mall.entity.po.IndexConfig;
import my_mall.result.PageResult;
import my_mall.result.Result;
import my_mall.service.IndexConfigService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "首页配置管理模块")
@RestController("adminIndexConfigController")
@RequestMapping("/api/admin/indexConfig")
@Slf4j
public class IndexConfigController {
    @Resource
    private IndexConfigService indexConfigService;

    @Operation(summary = "分页查询首页配置")
    @GetMapping
    @OperationLog(module = "管理端首页配置模块",type = "查询",description = "查询全部首页配置",
            recordParams = true,recordResult = true)
    public Result<PageResult> indexConfig(IndexPageDTO indexPageDTO) {
        PageResult pageResult=indexConfigService.getPage(indexPageDTO);
        return Result.success(pageResult);
    }

    @Operation(summary = "根据ID查询首页配置")
    @GetMapping("/{id}")
    @OperationLog(module = "管理端首页配置模块",type = "查询",description = "查询指定ID首页配置",
            recordParams = true,recordResult = true)
    public Result<IndexConfig> getIndexConfigById(@PathVariable Long id) {
        IndexConfig indexConfig=indexConfigService.getById(id);
        return Result.success(indexConfig);
    }

    @Operation(summary = "批量删除首页配置")
    @DeleteMapping
    @OperationLog(module = "管理端首页配置模块",type = "删除",description = "删除指定ID首页配置",
            recordParams = true,recordResult = true)
    public Result deleteIndexConfigById(@RequestParam List<Long> ids) {
        indexConfigService.delete(ids);
        return Result.success();
    }

    @Operation(summary = "更新首页配置")
    @PutMapping
    @OperationLog(module = "管理端首页配置模块",type = "更新",description = "更新首页配置",
            recordParams = true,recordResult = true)
    public Result updateIndexConfig(@RequestBody IndexConfigDTO indexConfigDTO) {
        indexConfigService.update(indexConfigDTO);
        return  Result.success();
    }

    @Operation(summary = "新增首页配置")
    @PostMapping
    @OperationLog(module = "管理端首页配置模块",type = "插入",description = "新增首页配置",
            recordParams = true,recordResult = true)
    public Result addIndexConfig(@RequestBody IndexConfigDTO indexConfigDTO) {
        indexConfigService.insert(indexConfigDTO);
        return Result.success();
    }
}
