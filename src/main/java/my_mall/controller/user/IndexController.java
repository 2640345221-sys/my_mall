package my_mall.controller.user;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import my_mall.annotation.OperationLog;
import my_mall.entity.po.Goods;
import my_mall.result.Result;
import my_mall.service.IndexConfigService;

@Tag(name = "首页配置模块")
@RestController("userIndexController")
@RequestMapping("/api/user/index")
@Slf4j
//用户端：首页的新品/热销/推荐商品
public class IndexController {
    @Resource
    private IndexConfigService indexConfigService;

    @GetMapping("/new")
    @Operation(summary = "获取新品商品")
    @OperationLog(module = "用户首页配置模块", type = "查询", description = "获取新品商品",
            recordParams = true, recordResult = true)
    public Result<List<Goods>> getNewGoods() {
        List<Goods> list = indexConfigService.getNewGoods();
        return Result.success(list);
    }

    @GetMapping("/popular")
    @Operation(summary = "获取热门商品")
    @OperationLog(module = "用户首页配置模块", type = "查询", description = "获取热门商品",
            recordParams = true, recordResult = true)
    public Result<List<Goods>> getPopularGoods() {
        List<Goods> list = indexConfigService.getPopularGoods();
        return Result.success(list);
    }

    @GetMapping("/recommend")
    @Operation(summary = "获取推荐商品")
    @OperationLog(module = "用户首页配置模块", type = "查询", description = "获取推荐商品",
            recordParams = true, recordResult = true)
    public Result<List<Goods>> getRecommendGoods() {
        List<Goods> list = indexConfigService.getRecommendGoods();
        return Result.success(list);
    }

}
