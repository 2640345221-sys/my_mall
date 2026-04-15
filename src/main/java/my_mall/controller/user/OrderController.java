package my_mall.controller.user;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import my_mall.entity.dto.OrderDTO;
import my_mall.entity.dto.UserAddressDTO;
import my_mall.result.Result;
import my_mall.service.OrderService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order")
@Slf4j
public class OrderController {
    @Resource
    private OrderService orderService;

    @PostMapping("/saveOrder")
    public Result saveOrder(@RequestBody OrderDTO orderDTO) {
        orderService.save(orderDTO);
        return Result.success();
    }
}
