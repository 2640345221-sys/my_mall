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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
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
    @Transactional
    public void save(OrderDTO orderDTO) {
        //获取地址和购物车数据
        Long addressId = orderDTO.getAddressId();
        List<Long> cartItemIds = orderDTO.getCartItemIds();
        UserAddress userAddress =addressMapper.getAddressById(addressId);
        //连接查询，将cart和goods关联
        List<OrderCartDTO> cartList=shoppingCartMapper.getWithGoods(cartItemIds);

        Integer totalPrice=0;
        for(OrderCartDTO cartItem:cartList){
            totalPrice+=cartItem.getCount()*cartItem.getPrice();
        }

        //从上述数据总结出order
        Order order =new Order();
        order.setUserId(TLUtils.getUserId());
        order.setCreateTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());
        order.setTotalPrice(totalPrice);
        order.setPayStatus((byte) 0);
        order.setOrderStatus((byte) 0);
        order.setPayType((byte) 0);
        order.setExtraInfo("");
        order.setOrderNo(generateOrderNo());
        orderMapper.insert(order);

        List<OrderItem> orderItemList=cartList.stream().map(x->{
            OrderItem orderItem=new OrderItem();
            BeanUtils.copyProperties(x,orderItem);
            orderItem.setCreateTime(LocalDateTime.now());
            orderItem.setOrderId(order.getId());
            return orderItem;
        }).collect(Collectors.toList());
        orderItemMapper.insertBatch(orderItemList);

        OrderAddress orderAddress=new OrderAddress();
        BeanUtils.copyProperties(userAddress,orderAddress);
        orderAddress.setOrderId(order.getId());
        orderAddress.setId(null);
        orderAddressMapper.insert(orderAddress);

        shoppingCartMapper.deleteBatch(cartItemIds);
    }

    @Override
    public void cancel(String orderNo) {
        Order order=new Order();
        order.setOrderStatus((byte)-2);
        order.setOrderNo(orderNo);
        order.setExtraInfo("用户取消订单");
        order.setUpdateTime(LocalDateTime.now());
        orderMapper.update(order);
    }

    @Override
    public void confirm(String orderNo) {
        Order order=new Order();
        order.setOrderStatus((byte)4);
        order.setOrderNo(orderNo);
        order.setUpdateTime(LocalDateTime.now());
        orderMapper.update(order);
    }

    /**
     * 生成一个根据时间戳和随机数的订单号
     * @return
     */
    public static String generateOrderNo() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
        String timePart = LocalDateTime.now().format(formatter);
        int random = ThreadLocalRandom.current().nextInt(1000);
        String randomPart = String.format("%03d", random);
        return timePart + randomPart;
    }
}
