package my_mall.service.impl;

import jakarta.annotation.Resource;
import my_mall.entity.dto.OrderCartDTO;
import my_mall.entity.dto.OrderDTO;
import my_mall.entity.po.*;
import my_mall.mapper.*;
import my_mall.service.OrderService;
import my_mall.utils.TLUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    @Resource
    private OrderMapper orderMapper;
    @Resource
    private OrderAddressMapper orderAddressMapper;
    @Resource
    private OrderItemMapper orderItemMapper;
    @Resource
    private AddressMapper addressMapper;
    @Resource
    private GoodsMapper goodsMapper;
    @Resource
    private ShoppingCartMapper shoppingCartMapper;
    @Override
    public void save(OrderDTO orderDTO) {

        Long addressId = orderDTO.getAddressId();
        List<Long> cartItemIds = orderDTO.getCartItemIds();
        UserAddress userAddress =addressMapper.getAddressById(addressId);
        List<OrderCartDTO> cartList=shoppingCartMapper.getWithGoods(cartItemIds);
        List<OrderItem> orderItemList=cartList.stream().map(x->{
            OrderItem orderItem=new OrderItem();
            BeanUtils.copyProperties(x,orderItem);
            orderItem.setCreateTime(LocalDateTime.now());
            return orderItem;
        }).collect(Collectors.toList());

        Order order =new Order();
        order.setUserId(TLUtils.getUserId());
        order.setCreateTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());


        orderItemMapper.insertBatch();

    }
}
