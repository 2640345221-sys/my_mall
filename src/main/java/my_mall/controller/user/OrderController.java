package my_mall.controller.user;

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

@RestController
@RequestMapping("/api/user/order")
@Slf4j
public class OrderController {
    @Resource
    private OrderService orderService;

    @PostMapping("/saveOrder")
    public Result saveOrder(@RequestBody OrderDTO orderDTO) {
        orderService.save(orderDTO);
        return Result.success();
    }

    @PutMapping("/{orderNo}/cancel")
    public Result cancelOrder(@PathVariable("orderNo") String orderNo) {
        orderService.cancel(orderNo);
        return Result.success();
    }

    @PutMapping("/{orderNo}/confirm")
    public Result confirmOrder(@PathVariable("orderNo") String orderNo) {
        orderService.confirm(orderNo);
        return Result.success();
    }

    @GetMapping("/{orderNo}")
    public Result<OrderDetailVO> getOrderDetail(@PathVariable("orderNo") String orderNo) {
        OrderDetailVO orderDetailVO=orderService.getOrderDetail(orderNo);
        return Result.success(orderDetailVO);
    }

    @GetMapping("page")
    public Result<PageResult> getPage(OrderPageDTO orderPageDTO) {
        PageResult pageResult=orderService.getPage(orderPageDTO);
        return Result.success(pageResult);
    }

    @GetMapping("/paySuccess")
    public Result paySuccess(OrderPayDTO orderPayDTO) {
        orderService.paySuccess(orderPayDTO);
        return Result.success();
    }
}
