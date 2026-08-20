package my_mall.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import my_mall.annotation.OperationLog;
import my_mall.entity.vo.SeckillGoodsVO;
import my_mall.result.Result;
import my_mall.service.SeckillGoodsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "用户秒杀商品模块")
@RestController("userSeckillGoodsController")
@RequestMapping("/api/user/seckill")
@Slf4j
//用户端：浏览进行中的秒杀商品
public class SeckillGoodsController {
    @Resource
    private SeckillGoodsService seckillGoodsService;

    @Operation(summary = "获取进行中的秒杀商品列表")
    @OperationLog(module = "用户秒杀商品模块", type = "查询", description = "浏览进行中的秒杀商品列表",
            recordParams = true, recordResult = true)
    @GetMapping("/list")
    public Result<List<SeckillGoodsVO>> list() {
        return Result.success(seckillGoodsService.listActiveForUser());
    }
}
