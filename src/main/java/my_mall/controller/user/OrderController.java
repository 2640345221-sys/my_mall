package my_mall.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import my_mall.entity.dto.OrderDTO;
import my_mall.entity.dto.OrderPageDTO;
import my_mall.entity.dto.OrderPayDTO;
import my_mall.entity.dto.UserAddressDTO;
import my_mall.entity.vo.OrderDetailVO;
import my_mall.result.PageResult;
import my_mall.result.Result;
import my_mall.service.OrderService;
import org.springframework.web.bind.annotation.*;

@Tag(name = "用户订单模块")
@RestController("userOrderController")
@RequestMapping("/api/user/order")
@Slf4j
public class OrderController {
    @Resource
    private OrderService orderService;

    @Operation(summary = "创建订单")
    @PostMapping("/saveOrder")
    public Result saveOrder(@RequestBody OrderDTO orderDTO) {
        orderService.save(orderDTO);
        return Result.success();
    }

    @Operation(summary = "取消订单")
    @PutMapping("/{orderNo}/cancel")
    public Result cancelOrder(@PathVariable("orderNo") String orderNo) {
        orderService.cancel(orderNo);
        return Result.success();
    }

    @Operation(summary = "确认收货")
    @PutMapping("/{orderNo}/confirm")
    public Result confirmOrder(@PathVariable("orderNo") String orderNo) {
        orderService.confirm(orderNo);
        return Result.success();
    }

    @Operation(summary = "获取订单详情")
    @GetMapping("/{orderNo}")
    public Result<OrderDetailVO> getOrderDetail(@PathVariable("orderNo") String orderNo) {
        OrderDetailVO orderDetailVO=orderService.getOrderDetail(orderNo);
        return Result.success(orderDetailVO);
    }

    @Operation(summary = "分页查询订单")
    @GetMapping("page")
    public Result<PageResult> getPage(OrderPageDTO orderPageDTO) {
        PageResult pageResult=orderService.getPage(orderPageDTO);
        return Result.success(pageResult);
    }

    @Operation(summary = "支付成功回调")
    @GetMapping("/paySuccess")
    public Result paySuccess(OrderPayDTO orderPayDTO) {
        orderService.paySuccess(orderPayDTO);
        return Result.success();
    }
}
