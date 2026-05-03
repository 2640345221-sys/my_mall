package my_mall.mapper;

import my_mall.entity.po.OrderAddress;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface OrderAddressMapper {

    void insert(OrderAddress orderAddress);
    @Select("select * from my_mall.order_address where order_id=#{id}")
    OrderAddress getByOrderId(Long id);
}
