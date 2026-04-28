package my_mall.mapper;

import my_mall.annotation.OperationFill;
import my_mall.entity.po.OrderItem;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderItemMapper {
    @OperationFill(fillCreateTime = true)
    void insert(OrderItem orderItem);

    void insertBatch(List<OrderItem> orderItemList);

    List<OrderItem> getByOrderId(Long id);

    List<OrderItem> getBatchByOrderId(List<Long> ids);
}
