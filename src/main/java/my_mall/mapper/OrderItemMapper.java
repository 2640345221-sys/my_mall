package my_mall.mapper;

import my_mall.entity.po.OrderItem;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderItemMapper {
    void insert(OrderItem orderItem);
}
