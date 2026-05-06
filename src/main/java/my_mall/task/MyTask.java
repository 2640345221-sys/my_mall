package my_mall.task;

import java.time.LocalDateTime;
import java.util.List;

import my_mall.entity.po.SeckillGoods;
import my_mall.mapper.SeckillGoodsMapper;
import my_mall.service.IndexConfigService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import my_mall.entity.po.Order;
import my_mall.enums.OrderStatusEnum;
import my_mall.exception.AutoConfirmException;
import my_mall.exception.TimeOutOrderException;
import my_mall.mapper.OrderMapper;

@Component
@Slf4j
public class MyTask {
    @Resource
    private OrderMapper orderMapper;
    @Resource
    private IndexConfigService indexConfigService;
    @Resource
    private SeckillGoodsMapper seckillGoodsMapper;
    @Resource
    private RedisTemplate redisTemplate;

    @Scheduled(cron = "0 */30 * * * ?")
    @Transactional
    public void processTimeoutOrder(){
        try {
            log.info("处理超时任务");
            List<Order> ordersList = orderMapper.getByStatusAndTime(0, LocalDateTime.now().plusMinutes(-15));

            ordersList.stream().forEach(order -> {
                order.setUpdateTime(LocalDateTime.now());
                order.setOrderStatus(OrderStatusEnum.ORDER_CLOSE_CONFIRM.getStatus());
            });

            if(!ordersList.isEmpty()) {
                orderMapper.updateBatch(ordersList);
                log.info("成功关闭 {} 个超时订单", ordersList.size());
            }
        } catch (Exception e) {
            throw new TimeOutOrderException("超时订单处理失败: " + e.getMessage());
        }
    }

    @Scheduled(cron="0 0 1 * * ?")
    @Transactional
    public void processOrderComplete(){
        try {
            log.info("自动确认完成任务");
            List<Order> ordersList = orderMapper.getByStatusAndTime(3, LocalDateTime.now().plusDays(-7));

            ordersList.stream().forEach(order -> {
                order.setOrderStatus(OrderStatusEnum.ORDER_SUCCESS.getStatus());
                order.setUpdateTime(LocalDateTime.now()); // 修复：添加更新时间
            });

            if(!ordersList.isEmpty()) {
                orderMapper.updateBatch(ordersList);
                log.info("成功完成 {} 个订单", ordersList.size());
            }
        } catch (Exception e) {
            throw new AutoConfirmException("自动确认完成订单失败: " + e.getMessage());
        }
    }

    @Scheduled(cron = "0 0 1 * * ?")
    public void resetIndexConfigTask() {
        log.info("开始执行首页配置重置任务");
        indexConfigService.resetIndexConfig();
        log.info("首页配置重置任务完成");
    }

    @Scheduled(cron = "0 */30 * * * ?")
    public void seckillGoodsResetTask() {
        List<SeckillGoods> activeList = seckillGoodsMapper.selectActiveList();
        for (SeckillGoods goods : activeList) {
            String stockKey = "seckill:stock:" + goods.getId();
            int stockInt=goods.getStockCount().intValue();
            redisTemplate.opsForValue().set(stockKey,stockInt);
            log.info("预热秒杀商品库存，ID: {}, 库存: {}", goods.getId(), goods.getStockCount());
        }
    }


}
