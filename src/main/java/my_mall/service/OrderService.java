package my_mall.service;

import my_mall.entity.dto.OrderDTO;
import my_mall.entity.dto.OrderPageDTO;
import my_mall.entity.dto.OrderPayDTO;
import my_mall.entity.vo.OrderDetailVO;
import my_mall.result.PageResult;

import java.util.List;

public interface OrderService {
    void save(OrderDTO orderDTO);

    void cancel(String orderNo);

    void confirm(String orderNo);

    OrderDetailVO getOrderDetail(String orderNo);

    PageResult getPage(OrderPageDTO orderPageDTO);

    void paySuccess(OrderPayDTO orderPayDTO);

    void setStatus(List<Long> ids, byte b);

    PageResult aGetPage(OrderPageDTO orderPageDTO);
}
