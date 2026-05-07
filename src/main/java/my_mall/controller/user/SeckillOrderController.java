package my_mall.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import my_mall.annotation.OperationLog;
import my_mall.entity.dto.PageDTO;
import my_mall.entity.dto.SeckillOrderDTO;
import my_mall.entity.po.SeckillOrder;
import my_mall.result.PageResult;
import my_mall.result.Result;
import my_mall.service.SeckillOrderService;
import my_mall.service.SeckillService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "秒杀订单管理模块")
@RestController("userSeckillOrderController")
@RequestMapping("/api/user/seckillOrder")
@Slf4j
public class SeckillOrderController {

    @Resource
    private SeckillOrderService seckillOrderService;
    @Resource
    private SeckillService seckillService;

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
    @Operation(summary = "处理秒杀商品")
    @OperationLog(module = "用户秒杀订单模块", type = "秒杀", description = "执行秒杀操作",
            recordParams = true, recordResult = true)
    public ResponseEntity<Result<Integer>> handle(@RequestBody SeckillOrderDTO seckillOrderDTO) {
        Integer result = seckillService.seckillWork(seckillOrderDTO);
        if (result == 1) {
            // 成功：200 OK
            return ResponseEntity.ok(Result.success(result));
        } else {
            // 失败：400 Bad Request，并在 body 中携带业务错误码
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Result.error("秒杀失败，代码：" + result));
        }
    }
}