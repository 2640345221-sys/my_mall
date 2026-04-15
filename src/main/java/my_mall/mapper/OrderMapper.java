package my_mall.mapper;

import my_mall.entity.po.Order;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper {
    void insert(Order order);

    void update(Order order);
}
