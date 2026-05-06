package my_mall.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import my_mall.annotation.OperationLog;
import my_mall.entity.dto.PageDTO;
import my_mall.entity.po.SeckillOrder;
import my_mall.result.PageResult;
import my_mall.result.Result;
import my_mall.service.SeckillOrderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "秒杀订单管理模块")
@RestController("adminSeckillOrderController")
@RequestMapping("/api/admin/seckillOrder")
@Slf4j
public class SeckillOrderController {

    @Resource
    private SeckillOrderService seckillOrderService;

    @GetMapping("/page")
    @Operation(summary = "分页查询秒杀订单")
    @OperationLog(module = "管理端秒杀订单模块", type = "查询", description = "分页查询秒杀订单列表",
            recordParams = true, recordResult = true)
    public Result<PageResult> page(PageDTO pageDTO) {
        PageResult pageResult = seckillOrderService.page(pageDTO);
        return Result.success(pageResult);
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询单个秒杀订单详情")
    @OperationLog(module = "管理端秒杀订单模块", type = "查询", description = "根据ID查询秒杀订单详情",
            recordParams = true, recordResult = true)
    public Result<SeckillOrder> getById(@PathVariable Long id) {
        SeckillOrder seckillOrder = seckillOrderService.getById(id);
        return Result.success(seckillOrder);
    }
}