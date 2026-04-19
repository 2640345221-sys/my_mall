package my_mall.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import my_mall.entity.dto.CarouselInsertDTO;
import my_mall.entity.dto.CarouselPageDTO;
import my_mall.entity.dto.CarouselUpdateDTO;
import my_mall.entity.po.Carousel;
import my_mall.result.PageResult;
import my_mall.result.Result;
import my_mall.service.CarouselService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "轮播图管理模块")
@RestController
@RequestMapping("/api/admin/carousels")
@Slf4j
public class CarouselController {
    @Resource
    private CarouselService carouselService;

    @Operation(summary = "分页查询轮播图")
    @GetMapping("/page")
    public Result<PageResult> getPage(CarouselPageDTO carouselPageDTO) {
        PageResult pageResult =carouselService.page(carouselPageDTO);
        return Result.success(pageResult);
    }

    @Operation(summary = "更新轮播图")
    @PutMapping
    public Result update(CarouselUpdateDTO carouselUpdateDTO) {
        carouselService.update(carouselUpdateDTO);
        return Result.success();
    }

    @Operation(summary = "新增轮播图")
    @PostMapping
    public Result insert(CarouselInsertDTO carouselInsertDTO) {
        carouselService.insert(carouselInsertDTO);
        return Result.success();
    }

    @Operation(summary = "根据ID查询轮播图")
    @GetMapping("/{id}")
    public Result<Carousel> getById(@PathVariable Long id) {
        Carousel carousel=carouselService.getById(id);
        return Result.success(carousel);
    }

    @Operation(summary = "批量删除轮播图")
    @DeleteMapping("/ids")
    public Result delete(@RequestParam List<Long> ids) {
        carouselService.delete(ids);
        return Result.success();
    }
}
