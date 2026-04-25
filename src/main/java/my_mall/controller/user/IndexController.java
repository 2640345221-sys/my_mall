package my_mall.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import my_mall.entity.po.Carousel;
import my_mall.entity.po.Goods;
import my_mall.result.Result;
import my_mall.service.CarouselService;
import my_mall.service.IndexConfigService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "首页配置模块")
@RestController("userIndexController")
@RequestMapping("/api/user/index")
@Slf4j
public class IndexController {
    @Resource
    private IndexConfigService indexConfigService;

    @Resource
    private CarouselService carouselService;

    @GetMapping("/new")
    @Operation(summary = "获取新品商品")
    public Result<List<Goods>> getNewGoods() {
        log.info("开始获取新品商品信息");
        List<Goods> list=indexConfigService.getNewGoods();
        return Result.success(list);
    }

    @GetMapping("/popular")
    @Operation(summary = "获取热门商品")
    public Result<List<Goods>> getPopularGoods() {
        log.info("开始获取热门商品信息");
        List<Goods> list=indexConfigService.getPopularGoods();
        return Result.success(list);
    }

    @GetMapping("/recommend")
    @Operation(summary = "获取推荐商品")
    public Result<List<Goods>> getRecommendGoods() {
        log.info("开始获取推荐商品信息");
        List<Goods> list=indexConfigService.getRecommendGoods();
        return Result.success(list);
    }

    @GetMapping("/carousel")
    @Operation(summary = "获取轮播图")
    public Result<List<Carousel>> getCarousel() {
        log.info("获取轮播图");
        List<Carousel> list=carouselService.getList();
        return Result.success(list);
    }
}
