package my_mall.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import my_mall.annotation.OperationLog;
import my_mall.entity.dto.SeckillGoodsPageDTO;
import my_mall.entity.po.SeckillGoods;
import my_mall.result.PageResult;
import my_mall.result.Result;
import my_mall.service.SeckillGoodsService;
import org.springframework.web.bind.annotation.*;

@Tag(name = "秒杀商品管理模块")
@RestController("adminSeckillGoodsController")
@RequestMapping("/api/admin/seckill")
@Slf4j
public class SeckillGoodsController {

    @Resource
    private SeckillGoodsService seckillGoodsService;

    @PostMapping
    @Operation(summary = "新增秒杀商品")
    @OperationLog(module = "管理端秒杀商品模块", type = "插入", description = "新增秒杀商品",
            recordParams = true, recordResult = true)
    public Result save(@RequestBody SeckillGoods seckillGoods) {
        seckillGoodsService.save(seckillGoods);
        return Result.success();
    }

    @OperationLog(module = "管理端秒杀商品模块", type = "更新", description = "修改秒杀商品",
            recordParams = true, recordResult = true)
    @PutMapping
    @Operation(summary = "更新秒杀商品")
    public Result update(@RequestBody SeckillGoods seckillGoods) {
        seckillGoodsService.update(seckillGoods);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除秒杀商品")
    @OperationLog(module = "管理端秒杀商品模块", type = "删除", description = "删除秒杀商品",
            recordParams = true, recordResult = true)
    public Result delete(@PathVariable Long id) {
        seckillGoodsService.deleteById(id);
        return Result.success();
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询秒杀商品")
    @OperationLog(module = "管理端秒杀商品模块", type = "查询", description = "分页查询秒杀商品列表",
            recordParams = true, recordResult = true)
    public Result<PageResult> page(SeckillGoodsPageDTO pageDTO) {
        PageResult pageResult = seckillGoodsService.page(pageDTO);
        return Result.success(pageResult);
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询单个秒杀商品")
    @OperationLog(module = "管理端秒杀商品模块", type = "查询", description = "根据ID查询秒杀商品详情",
            recordParams = true, recordResult = true)
    public Result<SeckillGoods> get(@PathVariable Long id) {
        SeckillGoods seckillGoods = seckillGoodsService.getById(id);
        return Result.success(seckillGoods);
    }

    @PostMapping("/status/{id}")
    @Operation(summary = "修改秒杀商品状态")
    @OperationLog(module = "管理端秒杀商品模块", type = "更新", description = "启用/禁用秒杀商品",
            recordParams = true, recordResult = true)
    public Result startOrStop(@PathVariable Long id, @RequestBody Integer status) {
        seckillGoodsService.updateStatus(id, status);
        return Result.success();
    }
}