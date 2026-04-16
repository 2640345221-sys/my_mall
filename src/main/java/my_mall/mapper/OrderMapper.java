package my_mall.mapper;

import com.github.pagehelper.Page;
import my_mall.entity.dto.OrderPageDTO;
import my_mall.entity.po.Order;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper {
    void insert(Order order);

    void update(Order order);

    Order getByOrderNo(String orderNo);

    Page<Order> getByUserId(OrderPageDTO orderPageDTO, Long userId);
}
