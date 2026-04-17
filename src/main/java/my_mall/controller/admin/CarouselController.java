package my_mall.controller.admin;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import my_mall.entity.dto.CarouselInsertDTO;
import my_mall.entity.dto.CarouselPageDTO;
import my_mall.entity.dto.CarouselUpdateDTO;
import my_mall.entity.po.Carousel;
import my_mall.result.PageResult;
import my_mall.result.Result;
import my_mall.service.CarouselService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/carousels")
@Slf4j
public class CarouselController {
    @Resource
    private CarouselService carouselService;
    @GetMapping("/page")
    public Result<PageResult> getPage(CarouselPageDTO carouselPageDTO) {
        PageResult pageResult =carouselService.page(carouselPageDTO);
        return Result.success(pageResult);
    }

    @PutMapping
    public Result update(CarouselUpdateDTO carouselUpdateDTO) {
        carouselService.update(carouselUpdateDTO);
        return Result.success();
    }

    @PostMapping
    public Result insert(CarouselInsertDTO carouselInsertDTO) {
        carouselService.insert(carouselInsertDTO);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<Carousel> getById(@PathVariable Long id) {
        Carousel carousel=carouselService.getById(id);
        return Result.success(carousel);
    }

    @DeleteMapping("/ids")
    public Result delete(@RequestParam List<Long> ids) {
        carouselService.delete(ids);
        return Result.success();
    }
}
