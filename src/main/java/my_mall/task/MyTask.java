package my_mall.task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import my_mall.service.IndexConfigService;
import my_mall.service.SeckillGoodsService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import my_mall.constant.MessageConstant;
import my_mall.entity.dto.StockDeductDTO;
import my_mall.entity.po.Order;
import my_mall.entity.po.OrderItem;
import my_mall.enums.OrderStatusEnum;
import my_mall.exception.AutoConfirmException;
import my_mall.exception.TimeOutOrderException;
import my_mall.mapper.GoodsMapper;
import my_mall.mapper.OrderItemMapper;
import my_mall.mapper.OrderMapper;

@Component
@Slf4j
public class MyTask {
    @Resource
    private OrderMapper orderMapper;
    @Resource
    private OrderItemMapper orderItemMapper;
    @Resource
    private GoodsMapper goodsMapper;
    @Resource
    private IndexConfigService indexConfigService;
    @Resource
    private SeckillGoodsService seckillGoodsService;

    //定时关闭超时未支付的订单：待支付超过15分钟就改为关闭状态
    @Scheduled(cron = "0 */30 * * * ?")
    @Transactional
    public void processTimeoutOrder(){
        try {
            log.info("处理超时任务");
            List<Order> ordersList = orderMapper.getByStatusAndTime(OrderStatusEnum.ORDER_PRE_PAY.getStatus(), LocalDateTime.now().plusMinutes(-15));

            for (Order order : ordersList) {
                //关闭超时订单前回补库存
                List<OrderItem> items = orderItemMapper.getByOrderId(order.getId());
                List<StockDeductDTO> stockRecoverList = items.stream()
                        .map(item -> new StockDeductDTO(item.getGoodsId(), item.getCount()))
                        .collect(Collectors.toList());
                goodsMapper.recoverStock(stockRecoverList);
                order.setUpdateTime(LocalDateTime.now());
                order.setOrderStatus(OrderStatusEnum.ORDER_CLOSE_CONFIRM.getStatus());
            }

            if(!ordersList.isEmpty()) {
                orderMapper.updateBatch(ordersList);
                log.info("成功关闭 {} 个超时订单", ordersList.size());
            }
        } catch (Exception e) {
            throw new TimeOutOrderException(MessageConstant.ORDER_TIMEOUT_HANDLE_ERROR + ": " + e.getMessage());
        }
    }

    //定时自动确认收货：已发货超过7天就改为交易完成
    @Scheduled(cron="0 0 1 * * ?")
    @Transactional
    public void processOrderComplete(){
        try {
            log.info("自动确认完成任务");
            List<Order> ordersList = orderMapper.getByStatusAndTime(OrderStatusEnum.ORDER_EXPRESS.getStatus(), LocalDateTime.now().plusDays(-7));

            ordersList.stream().forEach(order -> {
                order.setOrderStatus(OrderStatusEnum.ORDER_SUCCESS.getStatus());
                order.setUpdateTime(LocalDateTime.now());
            });

            if(!ordersList.isEmpty()) {
                orderMapper.updateBatch(ordersList);
                log.info("成功完成 {} 个订单", ordersList.size());
            }
        } catch (Exception e) {
            throw new AutoConfirmException(MessageConstant.ORDER_AUTO_CONFIRM_ERROR + ": " + e.getMessage());
        }
    }

    //定时重置首页配置：重新计算新品/热销/推荐商品
    @Scheduled(cron = "0 0 1 * * ?")
    public void resetIndexConfigTask() {
        log.info("开始执行首页配置重置任务");
        indexConfigService.resetIndexConfig();
        log.info("首页配置重置任务完成");
    }

    //定时自动上下架秒杀活动：到开始时间自动启用并预热库存，到结束时间自动禁用并清库存
    @Scheduled(cron = "0 * * * * ?")
    public void seckillGoodsAutoUpdateStatus() {
        seckillGoodsService.autoUpdateStatus();
    }

    //定时对账：以 Redis 库存为准修正数据库库存（异步落库失败时兜底）
    @Scheduled(cron = "0 */5 * * * ?")
    public void seckillStockReconcile() {
        seckillGoodsService.reconcileStock();
    }
}
