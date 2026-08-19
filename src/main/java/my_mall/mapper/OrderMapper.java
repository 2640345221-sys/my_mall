package my_mall.mapper;

import com.github.pagehelper.Page;
import my_mall.annotation.OperationFill;
import my_mall.entity.dto.OrderPageDTO;
import my_mall.entity.po.Order;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
//订单表的增删改查
public interface OrderMapper {
    @OperationFill(fillCreateTime = true,fillUpdateTime = true)
    void insert(Order order);
    @OperationFill(fillUpdateTime = true)
    void update(Order order);

    //按订单号查订单
    Order getByOrderNo(String orderNo);

    //用户分页查自己的订单
    Page<Order> getByUserId(OrderPageDTO orderPageDTO, Long userId);

    //批量修改订单状态
    void setStatus(List<Long> ids, Integer status, LocalDateTime updateTime);

    //管理员分页查所有用户订单
    Page<Order> aGetByUserId(OrderPageDTO orderPageDTO);

    //按id列表查订单
    List<Order> getByIds(List<Long> ids);

    //按状态和时间查订单（定时任务关单/确认收货用）
    List<Order> getByStatusAndTime(Integer orderStatus, LocalDateTime localDateTime);

    //批量更新订单
    void updateBatch(List<Order> ordersList);
}
