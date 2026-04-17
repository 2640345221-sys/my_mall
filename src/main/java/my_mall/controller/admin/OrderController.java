package my_mall.controller.admin;

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

@RestController
@RequestMapping("/api/admin/order")
@Slf4j
public class OrderController {
    @Resource
    private OrderService orderService;

    @GetMapping("/page")
    public Result<PageResult> getPage(OrderPageDTO orderPageDTO) {
        PageResult pageResult  =orderService.aGetPage(orderPageDTO);
        return Result.success(pageResult);
    }

    @GetMapping("/{orderNo}")
    public Result<OrderDetailVO> getOrder(@PathVariable("orderNo") String orderNo) {
        OrderDetailVO orderDetailVO=orderService.getOrderDetail(orderNo);
        return Result.success(orderDetailVO);
    }

    @PutMapping("/checkDone")
    public Result checkDone(@RequestParam List<Long> ids) {
        orderService.setStatus(ids,(byte)2);
        return Result.success();
    }

    @PutMapping("/checkOut")
    public Result checkOut(@RequestParam List<Long> ids) {
        orderService.setStatus(ids,(byte)3);
        return Result.success();
    }

    @PutMapping("/close")
    public Result close(@RequestParam List<Long> ids) {
        orderService.setStatus(ids,(byte)-3);
        return Result.success();
    }

}
