package my_mall.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
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
    public Result<PageResult> indexConfig(IndexPageDTO indexPageDTO) {
        log.info("开始查询首页配置{}",indexPageDTO);
        PageResult pageResult=indexConfigService.getPage(indexPageDTO);
        return Result.success(pageResult);
    }

    @Operation(summary = "根据ID查询首页配置")
    @GetMapping("/{id}")
    public Result<IndexConfig> getIndexConfigById(@PathVariable Long id) {
        log.info("获取id为{}的首页配置",id);
        IndexConfig indexConfig=indexConfigService.getById(id);
        return Result.success(indexConfig);
    }

    @Operation(summary = "批量删除首页配置")
    @DeleteMapping
    public Result deleteIndexConfigById(@RequestParam List<Long> ids) {
        log.info("开始删除首页配置,ids{}",ids);
        indexConfigService.delete(ids);
        return Result.success();
    }

    @Operation(summary = "更新首页配置")
    @PutMapping
    public Result updateIndexConfig(@RequestBody IndexConfigDTO indexConfigDTO) {
        log.info("更新首页配置{}",indexConfigDTO);
        indexConfigService.update(indexConfigDTO);
        return  Result.success();
    }

    @Operation(summary = "新增首页配置")
    @PostMapping
    public Result addIndexConfig(@RequestBody IndexConfigDTO indexConfigDTO) {
        log.info("新增首页配置{}",indexConfigDTO);
        indexConfigService.insert(indexConfigDTO);
        return Result.success();
    }
}
