package my_mall.task;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import my_mall.entity.po.Goods;
import my_mall.entity.po.IndexConfig;
import my_mall.entity.po.Order;
import my_mall.enums.IndexConfigTypeEnum;
import my_mall.enums.OrderStatusEnum;
import my_mall.exception.AutoConfirmException;
import my_mall.exception.TimeOutOrderException;
import my_mall.mapper.GoodsMapper;
import my_mall.mapper.IndexConfigMapper;
import my_mall.mapper.OrderMapper;

@Component
@Slf4j
public class MyTask {
    @Resource
    private OrderMapper orderMapper;
    @Resource
    private GoodsMapper goodsMapper;
    @Resource
    private IndexConfigMapper indexConfigMapper;

    @Scheduled(cron = "0 * * * * ?")
    @Transactional
    public void processTimeoutOrder(){
        try {
            log.info("处理超时任务");
            List<Order> ordersList = orderMapper.getByStatusAndTime(0, LocalDateTime.now().plusMinutes(-15));

            ordersList.stream().forEach(order -> {
                order.setUpdateTime(LocalDateTime.now());
                order.setOrderStatus(OrderStatusEnum.ORDER_CLOSE_BY_USER.getStatus());
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

    /**
     * 每天凌晨0点重新设置最新商品
     * 删除现有最新商品配置，根据商品创建时间获取最新的10个商品
     */
    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void resetLatestGoods() {
        try {
            log.info("开始重新设置最新商品");
            
            // 1. 删除现有的最新商品配置
            indexConfigMapper.deleteByType(IndexConfigTypeEnum.NEW_GOODS.getValue());
            
            // 2. 获取最新的10个商品（按创建时间倒序）
            List<Goods> latestGoods = goodsMapper.getLatestGoods(10);
            
            if (latestGoods.isEmpty()) {
                log.info("没有找到可用的最新商品");
                return;
            }
            
            // 3. 创建新的最新商品配置
            List<IndexConfig> newConfigs = latestGoods.stream()
                .map(goods -> IndexConfig.builder()
                    .name(goods.getName())
                    .type(IndexConfigTypeEnum.NEW_GOODS.getValue())
                    .goodsId(goods.getId())
                    .redirectUrl("/goods/detail/" + goods.getId())
                    .rank(0) // 可以根据需要设置排序
                    .createTime(LocalDateTime.now())
                    .updateTime(LocalDateTime.now())
                    .createUser(0) // 系统操作
                    .updateUser(0) // 系统操作
                    .build())
                .collect(java.util.stream.Collectors.toList());
            
            // 4. 批量插入新的配置
            indexConfigMapper.insertBatch(newConfigs);
            
            log.info("成功设置 {} 个最新商品", latestGoods.size());
        } catch (Exception e) {
            log.error("重新设置最新商品失败", e);
            throw new RuntimeException("重新设置最新商品失败: " + e.getMessage());
        }
    }

    /**
     * 每天凌晨0点重新设置热销商品
     * 删除现有热销商品配置，根据库存量获取最畅销的10个商品
     */
    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void resetHotGoods() {
        try {
            log.info("开始重新设置热销商品");
            
            // 1. 删除现有的热销商品配置
            indexConfigMapper.deleteByType(IndexConfigTypeEnum.POPULAR_GOODS.getValue());
            
            // 2. 获取最畅销的10个商品（按库存量倒序，库存量越大认为越畅销）
            List<Goods> hotGoods = goodsMapper.getHotGoods(10);
            
            if (hotGoods.isEmpty()) {
                log.info("没有找到可用的热销商品");
                return;
            }
            
            // 3. 创建新的热销商品配置
            List<IndexConfig> hotConfigs = hotGoods.stream()
                .map(goods -> IndexConfig.builder()
                    .name(goods.getName())
                    .type(IndexConfigTypeEnum.POPULAR_GOODS.getValue())
                    .goodsId(goods.getId())
                    .redirectUrl("/goods/detail/" + goods.getId())
                    .rank(goods.getStockNum()) // 使用库存量作为排序依据
                    .createTime(LocalDateTime.now())
                    .updateTime(LocalDateTime.now())
                    .createUser(0) // 系统操作
                    .updateUser(0) // 系统操作
                    .build())
                .collect(java.util.stream.Collectors.toList());
            
            // 4. 批量插入新的配置
            indexConfigMapper.insertBatch(hotConfigs);
            
            log.info("成功设置 {} 个热销商品", hotGoods.size());
        } catch (Exception e) {
            log.error("重新设置热销商品失败", e);
            throw new RuntimeException("重新设置热销商品失败: " + e.getMessage());
        }
    }

}
