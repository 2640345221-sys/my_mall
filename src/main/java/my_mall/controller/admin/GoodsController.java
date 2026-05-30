package my_mall.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import my_mall.annotation.OperationLog;
import my_mall.entity.dto.GoodsPageDTO;
import my_mall.entity.po.Goods;
import my_mall.entity.po.GoodsCategory;
import my_mall.entity.vo.GoodsDetailVO;
import my_mall.result.PageResult;
import my_mall.result.Result;
import my_mall.service.CategoryService;
import my_mall.service.GoodsService;
import my_mall.service.IndexConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "商品管理模块")
@RestController("adminGoodsController")
@RequestMapping("/api/admin/goods")
@Slf4j
public class GoodsController {
    @Resource
    private GoodsService goodsService;
    @Resource
    private IndexConfigService indexConfigService;

    @Operation(summary = "新增商品")
    @PostMapping
    @OperationLog(module = "管理端商品模块",type = "插入",description = "新增商品",
            recordParams = true,recordResult = true)
    public Result insert(@RequestBody Goods goods){
        goodsService.insert(goods);
        indexConfigService.resetIndexConfig();
        return Result.success();
    }

    @Operation(summary = "根据ID查询商品")
    @GetMapping("/{id}")
    @OperationLog(module = "管理端商品模块",type = "查询",description = "查询指定ID商品",
            recordParams = true,recordResult = true)
    public Result<GoodsDetailVO> getById(@PathVariable Long id){
        GoodsDetailVO goods=goodsService.getById(id);
        return Result.success(goods);
    }

    @Operation(summary = "分页查询商品")
    @GetMapping("/page")
    @OperationLog(module = "管理端商品模块",type = "查询",description = "分页查询商品",
            recordParams = true,recordResult = true)
    public Result<PageResult> page(GoodsPageDTO goodsPageDTO){
        PageResult pageResult=goodsService.page(goodsPageDTO);
        return Result.success(pageResult);
    }

    @Operation(summary = "修改商品上架状态")
    @PutMapping("/{sellStatus}")
    @OperationLog(module = "管理端商品模块",type = "更新",description = "修改商品上架状态",
            recordParams = true,recordResult = true)
    public Result updateSellStatus(@PathVariable Integer sellStatus,@RequestBody List<Long> ids){
        goodsService.updateStatus(sellStatus,ids);
        indexConfigService.resetIndexConfig();
        return Result.success();
    }

    @Operation(summary = "更新商品信息")
    @PutMapping
    @OperationLog(module = "管理端商品模块",type = "更新",description = "更新商品信息",
            recordParams = true,recordResult = true)
    public Result updateGoods(@RequestBody Goods goods){
        goodsService.updateGoods(goods);
        indexConfigService.resetIndexConfig();
        return Result.success();
    }

    @Operation(summary = "删除商品")
    @DeleteMapping("/{id}")
    @OperationLog(module = "管理端商品模块",type = "删除",description = "删除指定ID商品",
            recordParams = true,recordResult = true)
    public Result deleteGoods(@PathVariable Long id){
        goodsService.deleteGoods(id);
        indexConfigService.resetIndexConfig();
        return Result.success();
    }
}
