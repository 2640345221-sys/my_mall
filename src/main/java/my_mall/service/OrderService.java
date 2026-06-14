package my_mall.service;

import java.util.List;

import my_mall.entity.dto.OrderDTO;
import my_mall.entity.dto.OrderPageDTO;
import my_mall.entity.dto.OrderPayDTO;
import my_mall.entity.vo.OrderDetailVO;
import my_mall.result.PageResult;

public interface OrderService {
    String save(OrderDTO orderDTO);

    void cancel(String orderNo);

    void confirm(String orderNo);

    OrderDetailVO getOrderDetail(String orderNo);

    PageResult getPage(OrderPageDTO orderPageDTO);

    void paySuccess(OrderPayDTO orderPayDTO);

    PageResult aGetPage(OrderPageDTO orderPageDTO);

    void checkDone(List<Long> orderIds);

    void checkOut(List<Long> orderIds);

    void closeOrder(List<Long> orderIds);

    OrderDetailVO aGetOrderDetail(String orderNo);
}
