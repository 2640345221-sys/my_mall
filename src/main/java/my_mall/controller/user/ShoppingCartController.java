package my_mall.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import my_mall.entity.dto.CartItemDTO;
import my_mall.entity.dto.ShoppingCartDTO;
import my_mall.entity.dto.ShoppingCartItemDTO;
import my_mall.entity.dto.ShoppingCartPageDTO;
import my_mall.entity.vo.ShoppingCartItemVO;
import my_mall.result.PageResult;
import my_mall.result.Result;
import my_mall.service.ShoppingCartService;
import org.springframework.web.bind.annotation.*;

@Tag(name = "购物车模块")
@RestController
@RequestMapping("/api/user/cart")
@Slf4j
public class ShoppingCartController {
    @Resource
    private ShoppingCartService shoppingCartService;

    @Operation(summary = "添加商品到购物车")
    @PostMapping("/add")
    public Result addItem(@RequestBody CartItemDTO cartItemDTO) {
        log.info("新增购物车数据:{}", cartItemDTO);
        shoppingCartService.addItem(cartItemDTO);
        return Result.success();
    }

    @Operation(summary = "分页查询购物车")
    @GetMapping("/page")
    public Result<PageResult> getPage(ShoppingCartPageDTO pageDTO) {
        log.info("分页查询购物车");
        PageResult pageResult =shoppingCartService.getPage(pageDTO);
        return Result.success(pageResult);
    }

    @Operation(summary = "更新购物车商品数量")
    @PutMapping
    public Result update(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        log.info("开始更新购物车:{}", shoppingCartDTO);
        shoppingCartService.update(shoppingCartDTO);
        return Result.success();
    }

    @Operation(summary = "删除购物车商品")
    @DeleteMapping("/{cartItemId}")
    public Result delete(@PathVariable("cartItemId") Long cartItemId) {
        log.info("删除指定的购物车数据:{}", cartItemId);
        shoppingCartService.delete(cartItemId);
        return Result.success();
    }

    @Operation(summary = "获取指定购物车项")
    @GetMapping
    public Result<ShoppingCartItemVO> getCartItem(ShoppingCartItemDTO cartItemDTO) {
        log.info("获取指定的购物车数据;{}", cartItemDTO);
        ShoppingCartItemVO shoppingCartItemVO=shoppingCartService.getCartItem(cartItemDTO);
        return Result.success(shoppingCartItemVO);

    }

}
