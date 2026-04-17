package my_mall.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import jakarta.annotation.Resource;
import my_mall.entity.dto.OrderCartDTO;
import my_mall.entity.dto.OrderDTO;
import my_mall.entity.dto.OrderPageDTO;
import my_mall.entity.dto.OrderPayDTO;
import my_mall.entity.po.Order;
import my_mall.entity.po.OrderAddress;
import my_mall.entity.po.OrderItem;
import my_mall.entity.po.UserAddress;
import my_mall.entity.vo.OrderDetailVO;
import my_mall.mapper.AddressMapper;
import my_mall.mapper.GoodsMapper;
import my_mall.mapper.OrderAddressMapper;
import my_mall.mapper.OrderItemMapper;
import my_mall.mapper.OrderMapper;
import my_mall.mapper.ShoppingCartMapper;
import my_mall.result.PageResult;
import my_mall.service.OrderService;
import my_mall.utils.TLUtils;

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
        Long userId = TLUtils.getUserId();
        UserAddress userAddress =addressMapper.getAddressById(addressId);
        //连接查询，将cart和goods关联，同时校验购物车项是否属于当前用户
        List<OrderCartDTO> cartList=shoppingCartMapper.getWithGoods(cartItemIds, userId);

        Integer totalPrice=0;
        for(OrderCartDTO cartItem:cartList){
            totalPrice+=cartItem.getCount()*cartItem.getPrice();
        }

        //从上述数据总结出order
        Order order =new Order();
        order.setUserId(userId);
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

        shoppingCartMapper.deleteBatch(cartItemIds, userId);
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

    @Override
    public OrderDetailVO getOrderDetail(String orderNo) {
        Order order=orderMapper.getByOrderNo(orderNo);
        List<OrderItem> itemList=orderItemMapper.getByOrderId(order.getId());
        List<OrderCartDTO> list=itemList.stream().map(x->{
            OrderCartDTO cartItemDTO=new OrderCartDTO();
            BeanUtils.copyProperties(x,cartItemDTO);
            return cartItemDTO;
        }).collect(Collectors.toList());
        OrderDetailVO orderDetailVO=new OrderDetailVO();
        BeanUtils.copyProperties(order,orderDetailVO);
        orderDetailVO.setOrderCartDTO(list);
        return  orderDetailVO;
    }

    @Override
    public PageResult getPage(OrderPageDTO orderPageDTO) {
        Long userId = TLUtils.getUserId();
        PageHelper.startPage(orderPageDTO.getPageNumber(), orderPageDTO.getPageSize());
        Page<Order> page=orderMapper.getByUserId(orderPageDTO,userId);
        List<Long> ids=page.getResult().stream().map(x->x.getId()).collect(Collectors.toList());
        List<OrderItem> list=orderItemMapper.getBatchByOrderId(ids);
        Map<Long, List<OrderItem>> itemMap = list.stream()
                .collect(Collectors.groupingBy(OrderItem::getOrderId));
        List<Order>orders=page.getResult();

        List<OrderDetailVO> detailVOList = orders.stream().map(order -> {
            OrderDetailVO vo = new OrderDetailVO();
            BeanUtils.copyProperties(order, vo);
            List<OrderItem> items = itemMap.getOrDefault(order.getId(), Collections.emptyList());
            List<OrderCartDTO> cartDTOs = items.stream().map(item -> {
                OrderCartDTO dto = new OrderCartDTO();
                BeanUtils.copyProperties(item, dto);
                return dto;
            }).collect(Collectors.toList());
            vo.setOrderCartDTO(cartDTOs);
            return vo;
        }).collect(Collectors.toList());

        PageResult pageResult = new PageResult();
        pageResult.setTotal(page.getTotal());
        pageResult.setTotalPage(page.getPages());
        pageResult.setRecords(detailVOList);
        return pageResult;
    }

    @Override
    public void paySuccess(OrderPayDTO orderPayDTO) {
        Order order=new Order();
        BeanUtils.copyProperties(orderPayDTO,order);
        order.setPayTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());
        order.setPayStatus((byte) 1);
        order.setOrderStatus((byte) 1);

        orderMapper.update(order);
    }

    @Override
    public void setStatus(List<Long> ids, byte b) {
        orderMapper.setStatus(ids,b,LocalDateTime.now());
    }

    @Override
    public PageResult aGetPage(OrderPageDTO orderPageDTO) {
        Long userId = TLUtils.getUserId();
        PageHelper.startPage(orderPageDTO.getPageNumber(), orderPageDTO.getPageSize());
        Page<Order> page=orderMapper.aGetByUserId(orderPageDTO,userId);
        List<Long> ids=page.getResult().stream().map(x->x.getId()).collect(Collectors.toList());
        List<OrderItem> list=orderItemMapper.getBatchByOrderId(ids);
        Map<Long, List<OrderItem>> itemMap = list.stream()
                .collect(Collectors.groupingBy(OrderItem::getOrderId));
        List<Order>orders=page.getResult();

        List<OrderDetailVO> detailVOList = orders.stream().map(order -> {
            OrderDetailVO vo = new OrderDetailVO();
            BeanUtils.copyProperties(order, vo);
            List<OrderItem> items = itemMap.getOrDefault(order.getId(), Collections.emptyList());
            List<OrderCartDTO> cartDTOs = items.stream().map(item -> {
                OrderCartDTO dto = new OrderCartDTO();
                BeanUtils.copyProperties(item, dto);
                return dto;
            }).collect(Collectors.toList());
            vo.setOrderCartDTO(cartDTOs);
            return vo;
        }).collect(Collectors.toList());

        PageResult pageResult = new PageResult();
        pageResult.setTotal(page.getTotal());
        pageResult.setTotalPage(page.getPages());
        pageResult.setRecords(detailVOList);
        return pageResult;
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
