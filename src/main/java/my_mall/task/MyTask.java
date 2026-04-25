package my_mall.task;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import my_mall.entity.po.Order;
import my_mall.enums.OrderStatusEnum;
import my_mall.mapper.OrderMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class MyTask {
    @Resource
    private OrderMapper orderMapper;

    @Scheduled(cron = "0 * * * * ?")
    public void processTimeoutOrder(){
        log.info("处理超时任务");

        List<Order> ordersList =orderMapper.getByStatusAndTime(0, LocalDateTime.now().plusMinutes(-15));

        ordersList.stream().forEach(order -> {
            order.setUpdateTime(LocalDateTime.now());
            order.setOrderStatus(OrderStatusEnum.ORDER_CLOSE_BY_USER.getStatus());
        });
        if(!ordersList.isEmpty())
            orderMapper.updateBatch(ordersList);
    }

    @Scheduled(cron="0 0 1 * * ?")
    public void processOrderComplete(){
        log.info("自动确认完成任务");
        List<Order> ordersList =orderMapper.getByStatusAndTime(3, LocalDateTime.now().plusDays(-7));

        ordersList.stream().forEach(order -> order.setOrderStatus(OrderStatusEnum.ORDER_SUCCESS.getStatus()));
        if(!ordersList.isEmpty())
            orderMapper.updateBatch(ordersList);
    }

}
