package my_mall.controller.user;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import my_mall.annotation.OperationLog;
import my_mall.entity.dto.CartItemDTO;
import my_mall.entity.dto.ShoppingCartDTO;
import my_mall.entity.dto.ShoppingCartItemDTO;
import my_mall.entity.dto.ShoppingCartPageDTO;
import my_mall.entity.vo.ShoppingCartItemVO;
import my_mall.result.PageResult;
import my_mall.result.Result;
import my_mall.service.ShoppingCartService;

@Tag(name = "购物车模块")
@RestController
@RequestMapping("/api/user/cart")
@Slf4j
public class ShoppingCartController {
    @Resource
    private ShoppingCartService shoppingCartService;

    @Operation(summary = "添加商品到购物车")
    @OperationLog(module = "用户购物车模块", type = "新增", description = "添加商品到购物车",
            recordParams = true, recordResult = true)
    @PostMapping("/add")
    public Result addItem(@RequestBody CartItemDTO cartItemDTO) {
        shoppingCartService.addItem(cartItemDTO);
        return Result.success();
    }

    @Operation(summary = "分页查询购物车")
    @OperationLog(module = "用户购物车模块", type = "查询", description = "分页查询购物车",
            recordParams = true, recordResult = true)
    @GetMapping("/page")
    public Result<PageResult> getPage(ShoppingCartPageDTO pageDTO) {
        PageResult pageResult =shoppingCartService.getPage(pageDTO);
        return Result.success(pageResult);
    }

    @Operation(summary = "更新购物车商品数量")
    @OperationLog(module = "用户购物车模块", type = "更新", description = "更新购物车商品数量",
            recordParams = true, recordResult = true)
    @PutMapping
    public Result update(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        shoppingCartService.update(shoppingCartDTO);
        return Result.success();
    }

    @Operation(summary = "删除购物车商品")
    @OperationLog(module = "用户购物车模块", type = "删除", description = "删除购物车商品",
            recordParams = true, recordResult = true)
    @DeleteMapping("/{cartItemId}")
    public Result delete(@PathVariable("cartItemId") Long cartItemId) {
        shoppingCartService.delete(cartItemId);
        return Result.success();
    }

    @Operation(summary = "获取指定购物车项")
    @OperationLog(module = "用户购物车模块", type = "查询", description = "获取指定购物车项",
            recordParams = true, recordResult = true)
    @GetMapping
    public Result<ShoppingCartItemVO> getCartItem(ShoppingCartItemDTO cartItemDTO) {
        ShoppingCartItemVO shoppingCartItemVO=shoppingCartService.getCartItem(cartItemDTO);
        return Result.success(shoppingCartItemVO);

    }

}
