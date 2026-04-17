package my_mall.controller.admin;

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

@RestController("adminGoodsController")
@RequestMapping("/api/admin/goods")
@Slf4j
public class GoodsController {
    @Autowired
    private GoodsService goodsService;

    @PostMapping
    public Result insert(@RequestBody Goods goods){
        goodsService.insert(goods);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<GoodsDetailVO> getById(@PathVariable Long id){
        GoodsDetailVO goods=goodsService.getById(id);
        return Result.success(goods);
    }

    @GetMapping("/page")
    public Result<PageResult> page(GoodsPageDTO  goodsPageDTO){
        PageResult pageResult=goodsService.page(goodsPageDTO);
        return Result.success(pageResult);
    }

    @PutMapping("/{sellStatus}")
    public Result updateSellStatus(@PathVariable Byte sellStatus,@RequestBody List<Long> ids){
        goodsService.updateStatus(sellStatus,ids);
        return Result.success();
    }

    @PutMapping
    public Result updateGoods(@RequestBody Goods goods){
        goodsService.updateGoods(goods);
        return Result.success();
    }
}
