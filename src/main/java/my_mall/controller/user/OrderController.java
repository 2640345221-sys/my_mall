package my_mall.controller.user;

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
import my_mall.entity.dto.OrderDTO;
import my_mall.entity.dto.OrderPageDTO;
import my_mall.entity.dto.OrderPayDTO;
import my_mall.entity.vo.OrderDetailVO;
import my_mall.result.PageResult;
import my_mall.result.Result;
import my_mall.service.OrderService;

@Tag(name = "用户订单模块")
@RestController("userOrderController")
@RequestMapping("/api/user/order")
@Slf4j
public class OrderController {
    @Resource
    private OrderService orderService;

    @Operation(summary = "创建订单")
    @OperationLog(module = "用户订单模块", type = "创建", description = "创建订单",
            recordParams = true, recordResult = true)
    @PostMapping("/saveOrder")
    public Result saveOrder(@RequestBody OrderDTO orderDTO) {
        orderService.save(orderDTO);
        return Result.success();
    }

    @Operation(summary = "取消订单")
    @OperationLog(module = "用户订单模块", type = "更新", description = "取消订单",
            recordParams = true, recordResult = true)
    @PutMapping("/{orderNo}/cancel")
    public Result cancelOrder(@PathVariable("orderNo") String orderNo) {
        orderService.cancel(orderNo);
        return Result.success();
    }

    @Operation(summary = "确认收货")
    @OperationLog(module = "用户订单模块", type = "更新", description = "确认收货",
            recordParams = true, recordResult = true)
    @PutMapping("/{orderNo}/confirm")
    public Result confirmOrder(@PathVariable("orderNo") String orderNo) {
        orderService.confirm(orderNo);
        return Result.success();
    }

    @Operation(summary = "获取订单详情")
    @OperationLog(module = "用户订单模块", type = "查询", description = "获取订单详情",
            recordParams = true, recordResult = true)
    @GetMapping("/{orderNo}")
    public Result<OrderDetailVO> getOrderDetail(@PathVariable("orderNo") String orderNo) {
        OrderDetailVO orderDetailVO=orderService.getOrderDetail(orderNo);
        return Result.success(orderDetailVO);
    }

    @Operation(summary = "分页查询订单")
    @OperationLog(module = "用户订单模块", type = "查询", description = "分页查询订单",
            recordParams = true, recordResult = true)
    @GetMapping("/page")
    public Result<PageResult> getPage(OrderPageDTO orderPageDTO) {
        PageResult pageResult=orderService.getPage(orderPageDTO);
        return Result.success(pageResult);
    }

    @Operation(summary = "支付成功回调")
    @OperationLog(module = "用户订单模块", type = "更新", description = "支付成功回调",
            recordParams = true, recordResult = true)
    @GetMapping("/paySuccess")
    public Result paySuccess(OrderPayDTO orderPayDTO) {
        orderService.paySuccess(orderPayDTO);
        return Result.success();
    }
}
