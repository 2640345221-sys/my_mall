package my_mall.controller.admin;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import my_mall.annotation.OperationLog;
import my_mall.entity.dto.OrderPageDTO;
import my_mall.entity.vo.OrderDetailVO;
import my_mall.result.PageResult;
import my_mall.result.Result;
import my_mall.service.OrderService;

@Tag(name = "订单管理模块")
@RestController("adminOrderController")
@RequestMapping("/api/admin/order")
@Slf4j
public class OrderController {
    @Resource
    private OrderService orderService;

    @Operation(summary = "分页查询订单")
    @OperationLog(module = "管理端订单模块", type = "查询", description = "分页查询订单",
            recordParams = true, recordResult = true)
    @GetMapping("/page")
    public Result<PageResult> getPage(OrderPageDTO orderPageDTO) {
        PageResult pageResult  =orderService.aGetPage(orderPageDTO);
        return Result.success(pageResult);
    }

    @Operation(summary = "获取订单详情")
    @OperationLog(module = "管理端订单模块", type = "查询", description = "获取订单详情",
            recordParams = true, recordResult = true)
    @GetMapping("/{orderNo}")
    public Result<OrderDetailVO> getOrder(@PathVariable("orderNo") String orderNo) {
        OrderDetailVO orderDetailVO=orderService.aGetOrderDetail(orderNo);
        return Result.success(orderDetailVO);
    }

    @Operation(summary = "确认收货")
    @OperationLog(module = "管理端订单模块", type = "更新", description = "确认收货",
            recordParams = true, recordResult = true)
    @PutMapping("/checkDone")
    public Result checkDone(@RequestBody List<Long> ids) {
        orderService.checkDone(ids);
        return Result.success();
    }

    @Operation(summary = "订单出库")
    @OperationLog(module = "管理端订单模块", type = "更新", description = "订单出库",
            recordParams = true, recordResult = true)
    @PutMapping("/checkOut")
    public Result checkOut(@RequestBody List<Long> ids) {
        orderService.checkOut(ids);
        return Result.success();
    }

    @Operation(summary = "关闭订单")
    @OperationLog(module = "管理端订单模块", type = "更新", description = "关闭订单",
            recordParams = true, recordResult = true)
    @PutMapping("/close")
    public Result close(@RequestBody List<Long> ids) {
        orderService.closeOrder(ids);
        return Result.success();
    }

}
