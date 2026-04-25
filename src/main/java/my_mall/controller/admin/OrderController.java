package my_mall.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import my_mall.entity.dto.OrderDTO;
import my_mall.entity.dto.OrderPageDTO;
import my_mall.entity.po.Order;
import my_mall.entity.vo.OrderDetailVO;
import my_mall.result.PageResult;
import my_mall.result.Result;
import my_mall.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "订单管理模块")
@RestController("adminOrderController")
@RequestMapping("/api/admin/order")
@Slf4j
public class OrderController {
    @Resource
    private OrderService orderService;

    @Operation(summary = "分页查询订单")
    @GetMapping("/page")
    public Result<PageResult> getPage(OrderPageDTO orderPageDTO) {
        log.info("开始查询订单系统{}",orderPageDTO);
        PageResult pageResult  =orderService.aGetPage(orderPageDTO);
        return Result.success(pageResult);
    }

    @Operation(summary = "获取订单详情")
    @GetMapping("/{orderNo}")
    public Result<OrderDetailVO> getOrder(@PathVariable("orderNo") String orderNo) {
        log.info("获取订单号为{}的订单详情", orderNo);
        OrderDetailVO orderDetailVO=orderService.getOrderDetail(orderNo);
        return Result.success(orderDetailVO);
    }

    @Operation(summary = "确认收货")
    @PutMapping("/checkDone")
    public Result checkDone(@RequestParam List<Long> ids) {
        log.info("下列订单号的订单将被确认{}",ids);
        orderService.checkDone(ids);
        return Result.success();
    }

    @Operation(summary = "订单出库")
    @PutMapping("/checkOut")
    public Result checkOut(@RequestParam List<Long> ids) {
        log.info("下列订单号的订单将被出库{}",ids);
        orderService.checkOut(ids);
        return Result.success();
    }

    @Operation(summary = "关闭订单")
    @PutMapping("/close")
    public Result close(@RequestParam List<Long> ids) {
        log.info("下列订单号的订单将被关闭{}",ids);
        orderService.closeOrder(ids);
        return Result.success();
    }

}
