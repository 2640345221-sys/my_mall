package my_mall.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import my_mall.annotation.OperationLog;
import my_mall.config.RabbitMQConfig;
import my_mall.entity.dto.PageDTO;
import my_mall.entity.dto.SeckillMessage;
import my_mall.entity.dto.SeckillOrderDTO;
import my_mall.entity.po.SeckillOrder;
import my_mall.result.PageResult;
import my_mall.result.Result;
import my_mall.service.SeckillOrderService;
import my_mall.utils.TLUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.*;

@Tag(name = "秒杀订单管理模块")
@RestController("userSeckillOrderController")
@RequestMapping("/api/user/seckillOrder")
@Slf4j
public class SeckillOrderController {

    @Resource
    private SeckillOrderService seckillOrderService;
    @Resource
    private RabbitTemplate rabbitTemplate;

    @GetMapping("result")
    @Operation(summary = "查询秒杀结果")
    @OperationLog(module = "用户秒杀订单模块", type = "查询", description = "查询秒杀结果",
            recordParams = true, recordResult = true)
    public Result<SeckillOrder> result(@RequestParam Long id) {
        SeckillOrder seckillOrder = seckillOrderService.result(id);
        return Result.success(seckillOrder);
    }

    @GetMapping("/list")
    @Operation(summary = "查询秒杀订单列表")
    @OperationLog(module = "用户秒杀订单模块", type = "查询", description = "分页查询用户秒杀订单列表",
            recordParams = true, recordResult = true)
    public Result<PageResult> list(PageDTO pageDTO) {
        PageResult pageResult = seckillOrderService.list(pageDTO);
        return Result.success(pageResult);
    }

    @PostMapping
    @Operation(summary = "提交秒杀请求")
    @OperationLog(module = "用户秒杀订单模块", type = "秒杀", description = "提交秒杀请求至队列",
            recordParams = true, recordResult = true)
    public Result<String> handle(@RequestBody SeckillOrderDTO seckillOrderDTO) {
        Long userId = TLUtils.getUserId();
        SeckillMessage message = new SeckillMessage();
        message.setUserId(userId);
        message.setSeckillGoodsId(seckillOrderDTO.getSeckillGoodsId());
        message.setAddressId(seckillOrderDTO.getAddressId());
        message.setCount(seckillOrderDTO.getCount());
        rabbitTemplate.convertAndSend(RabbitMQConfig.SECKILL_REQUEST_QUEUE, message);
        return Result.success("秒杀请求已提交，请稍后查询结果");
    }
}
