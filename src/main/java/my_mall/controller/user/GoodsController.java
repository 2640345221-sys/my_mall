package my_mall.controller.user;

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

@RestController
@RequestMapping("/api/user/goods")
@Slf4j
public class GoodsController {
    @Resource
    private GoodsService goodsService;

    @GetMapping("/detail/{goodsId}")
    public Result<GoodsDetailVO> getGoodsDetail(@PathVariable("goodsId") Long goodsId){
        GoodsDetailVO goodsDetailVO=goodsService.getGoodsDetail(goodsId);
        return  Result.success(goodsDetailVO);
    }

    @GetMapping("/search")
    public Result<PageResult> search(GoodsPageSearchDTO goodsPageSearchDTO){
        PageResult pageResult=goodsService.search(goodsPageSearchDTO);
        return Result.success(pageResult);
    }
}
