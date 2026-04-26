package my_mall.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import my_mall.annotation.OperationLog;
import my_mall.entity.dto.CarouselDTO;
import my_mall.entity.dto.CarouselPageDTO;
import my_mall.entity.po.Carousel;
import my_mall.result.PageResult;
import my_mall.result.Result;
import my_mall.service.CarouselService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "轮播图管理模块")
@RestController
@RequestMapping("/api/admin/carousel")
@Slf4j
public class CarouselController {
    @Resource
    private CarouselService carouselService;

    @Operation(summary = "分页查询轮播图")
    @GetMapping("/page")
    @OperationLog(module = "管理端轮播图模块",type = "查询",description = "分页查询轮播图",
    recordParams = true,recordResult = true)
    public Result<PageResult> getPage(CarouselPageDTO carouselPageDTO) {
        PageResult pageResult =carouselService.page(carouselPageDTO);
        return Result.success(pageResult);
    }

    @Operation(summary = "更新轮播图")
    @PutMapping
    @OperationLog(module = "管理端轮播图模块",type = "更新",description = "更新轮播图",
            recordParams = true,recordResult = true)
    public Result update(CarouselDTO carouselUpdateDTO) {
        carouselService.update(carouselUpdateDTO);
        return Result.success();
    }

    @Operation(summary = "新增轮播图")
    @PostMapping
    @OperationLog(module = "管理端轮播图模块",type = "插入",description = "新增轮播图",
            recordParams = true,recordResult = true)
    public Result insert(@RequestBody CarouselDTO carouselDTO) {
        carouselService.insert(carouselDTO);
        return Result.success();
    }

    @Operation(summary = "根据ID查询轮播图")
    @GetMapping("/{id}")
    @OperationLog(module = "管理端轮播图模块",type = "查询",description = "根据ID查询轮播图",
            recordParams = true,recordResult = true)
    public Result<Carousel> getById(@PathVariable Long id) {
        Carousel carousel=carouselService.getById(id);
        return Result.success(carousel);
    }

    @Operation(summary = "批量删除轮播图")
    @DeleteMapping
    @OperationLog(module = "管理端轮播图模块",type = "删除",description = "删除指定轮播图",
            recordParams = true,recordResult = true)
    public Result delete(@RequestParam List<Long> ids) {
        carouselService.delete(ids);
        return Result.success();
    }
}
