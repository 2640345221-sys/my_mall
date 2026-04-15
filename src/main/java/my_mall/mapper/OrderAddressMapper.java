package my_mall.mapper;

import my_mall.entity.po.OrderAddress;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderAddressMapper {

    void insert(OrderAddress orderAddress);
}
