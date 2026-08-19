package my_mall.mapper;

import my_mall.entity.po.OrderAddress;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
//订单收货地址表的增删改查
public interface OrderAddressMapper {

    void insert(OrderAddress orderAddress);

    OrderAddress getByOrderId(Long id);

    //按订单id列表批量查收货地址
    List<OrderAddress> getByOrderIds(@Param("ids") List<Long> ids);
}
