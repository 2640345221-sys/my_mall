package my_mall.service;

import my_mall.entity.dto.OrderDTO;

public interface OrderService {
    void save(OrderDTO orderDTO);

    void cancel(String orderNo);

    void confirm(String orderNo);
}
