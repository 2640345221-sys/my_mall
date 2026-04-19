package my_mall.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
    @Autowired
    private GoodsService goodsService;

    @Operation(summary = "新增商品")
    @PostMapping
    public Result insert(@RequestBody Goods goods){
        goodsService.insert(goods);
        return Result.success();
    }

    @Operation(summary = "根据ID查询商品")
    @GetMapping("/{id}")
    public Result<GoodsDetailVO> getById(@PathVariable Long id){
        GoodsDetailVO goods=goodsService.getById(id);
        return Result.success(goods);
    }

    @Operation(summary = "分页查询商品")
    @GetMapping("/page")
    public Result<PageResult> page(GoodsPageDTO  goodsPageDTO){
        PageResult pageResult=goodsService.page(goodsPageDTO);
        return Result.success(pageResult);
    }

    @Operation(summary = "修改商品上架状态")
    @PutMapping("/{sellStatus}")
    public Result updateSellStatus(@PathVariable Byte sellStatus,@RequestBody List<Long> ids){
        goodsService.updateStatus(sellStatus,ids);
        return Result.success();
    }

    @Operation(summary = "更新商品信息")
    @PutMapping
    public Result updateGoods(@RequestBody Goods goods){
        goodsService.updateGoods(goods);
        return Result.success();
    }
}
