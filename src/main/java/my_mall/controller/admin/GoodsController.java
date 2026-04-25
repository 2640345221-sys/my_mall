package my_mall.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import my_mall.entity.dto.GoodsPageDTO;
import my_mall.entity.po.Goods;
import my_mall.entity.po.GoodsCategory;
import my_mall.entity.vo.GoodsDetailVO;
import my_mall.result.PageResult;
import my_mall.result.Result;
import my_mall.service.CategoryService;
import my_mall.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "商品管理模块")
@RestController("adminGoodsController")
@RequestMapping("/api/admin/goods")
@Slf4j
public class GoodsController {
    @Resource
    private GoodsService goodsService;

    @Operation(summary = "新增商品")
    @PostMapping
    public Result insert(@RequestBody Goods goods){
        log.info("开始新增商品{}",goods);
        goodsService.insert(goods);
        return Result.success();
    }

    @Operation(summary = "根据ID查询商品")
    @GetMapping("/{id}")
    public Result<GoodsDetailVO> getById(@PathVariable Long id){
        log.info("查询id为{}的商品",id);
        GoodsDetailVO goods=goodsService.getById(id);
        return Result.success(goods);
    }

    @Operation(summary = "分页查询商品")
    @GetMapping("/page")
    public Result<PageResult> page(GoodsPageDTO goodsPageDTO){
        log.info("开始查询商品{}",goodsPageDTO);
        PageResult pageResult=goodsService.page(goodsPageDTO);
        return Result.success(pageResult);
    }

    @Operation(summary = "修改商品上架状态")
    @PutMapping("/{sellStatus}")
    public Result updateSellStatus(@PathVariable Integer sellStatus,@RequestBody List<Long> ids){
        log.info("修改id为{}的商品的上架状态为{}",ids,sellStatus);
        goodsService.updateStatus(sellStatus,ids);
        return Result.success();
    }

    @Operation(summary = "更新商品信息")
    @PutMapping
    public Result updateGoods(@RequestBody Goods goods){
        log.info("更新商品信息{}",goods);
        goodsService.updateGoods(goods);
        return Result.success();
    }

    @Operation(summary = "删除商品")
    @DeleteMapping("/{id}")
    public Result deleteGoods(@PathVariable Long id){
        log.info("开始删除商品,id:{}",id);
        goodsService.deleteGoods(id);
        return Result.success();
    }
}
