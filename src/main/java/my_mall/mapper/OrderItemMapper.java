package my_mall.mapper;

import my_mall.entity.po.OrderItem;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderItemMapper {
    void insert(OrderItem orderItem);

    void insertBatch(List<OrderItem> orderItemList);
}
