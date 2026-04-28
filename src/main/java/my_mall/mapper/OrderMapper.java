package my_mall.mapper;

import com.github.pagehelper.Page;
import my_mall.annotation.OperationFill;
import my_mall.entity.dto.OrderPageDTO;
import my_mall.entity.po.Order;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface OrderMapper {
    @OperationFill(fillCreateTime = true,fillUpdateTime = true)
    void insert(Order order);
    @OperationFill(fillUpdateTime = true)
    void update(Order order);

    Order getByOrderNo(String orderNo);

    Page<Order> getByUserId(OrderPageDTO orderPageDTO, Long userId);

    void setStatus(List<Long> ids, Integer b, LocalDateTime now);

    Page<Order> aGetByUserId(OrderPageDTO orderPageDTO, Long userId);

    List<Order> getByIds(List<Long> ids);

    List<Order> getByStatusAndTime(Integer orderStatus, LocalDateTime localDateTime);

    void updateBatch(List<Order> ordersList);
}
