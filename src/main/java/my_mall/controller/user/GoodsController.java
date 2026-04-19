package my_mall.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import my_mall.entity.dto.GoodsPageSearchDTO;
import my_mall.entity.vo.GoodsDetailVO;
import my_mall.result.PageResult;
import my_mall.result.Result;
import my_mall.service.GoodsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "用户商品模块")
@RestController("userGoodsController")
@RequestMapping("/api/user/goods")
@Slf4j
public class GoodsController {
    @Resource
    private GoodsService goodsService;

    @Operation(summary = "获取商品详情")
    @GetMapping("/detail/{goodsId}")
    public Result<GoodsDetailVO> getGoodsDetail(@PathVariable("goodsId") Long goodsId){
        GoodsDetailVO goodsDetailVO=goodsService.getGoodsDetail(goodsId);
        return  Result.success(goodsDetailVO);
    }

    @Operation(summary = "搜索商品")
    @GetMapping("/search")
    public Result<PageResult> search(GoodsPageSearchDTO goodsPageSearchDTO){
        PageResult pageResult=goodsService.search(goodsPageSearchDTO);
        return Result.success(pageResult);
    }
}
