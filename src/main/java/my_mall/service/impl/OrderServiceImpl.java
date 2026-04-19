package my_mall.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import lombok.SneakyThrows;
import my_mall.common.OrderStatusEnum;
import my_mall.entity.dto.*;
import my_mall.entity.po.*;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import jakarta.annotation.Resource;
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
    @SneakyThrows
    @Override
    @Transactional
    public void save(OrderDTO orderDTO) {
        //获取地址和购物车数据
        Long addressId = orderDTO.getAddressId();
        List<Long> cartItemIds = orderDTO.getCartItemIds();
        Long userId = TLUtils.getUserId();
        UserAddress userAddress =addressMapper.getAddressById(addressId);
        if(userAddress==null||!userAddress.getUserId().equals(userId)){
            throw new Exception("地址不存在或无权限");
        }
        //连接查询，将cart和goods关联，同时校验购物车项是否属于当前用户
        List<OrderCartDTO> cartList=shoppingCartMapper.getWithGoods(cartItemIds, userId);
        if(cartList==null|| cartList.isEmpty()){
            throw new Exception("购物车数据为空");
        }
        List<StockDeductDTO> list=new ArrayList<>();

        Integer totalPrice=0;
        for(OrderCartDTO cartItem:cartList){
            Goods goods=goodsMapper.getById(cartItem.getGoodsId());
            if(goods==null){
                throw new Exception("商品不存在"+cartItem.getGoodsId());
            }
            if(!goods.getSellStatus()){
                throw new Exception("商品已下架"+cartItem.getGoodsName());
            }
            if(cartItem.getCount()>goods.getStockNum()){
                throw new Exception("商品库存不足"+cartItem.getGoodsName());
            }
            totalPrice+=cartItem.getCount()*goods.getSellingPrice();
            cartItem.setPrice(goods.getSellingPrice());
            list.add(new StockDeductDTO(goods.getId(),cartItem.getCount()));
        }

        goodsMapper.deductStock(list);

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

    @SneakyThrows
    @Override
    public void cancel(String orderNo) {
        Long userId=TLUtils.getUserId();
        Order order=orderMapper.getByOrderNo(orderNo);
        if(order==null){
            throw new Exception("订单不存在");
        }
        if(!order.getUserId().equals(userId)){
            throw new Exception("无权操作此订单");
        }
        if(!OrderStatusEnum.canCancel(order.getOrderStatus())){
            throw new Exception("该订单无法取消");
        }

        List<OrderItem> items = orderItemMapper.getByOrderId(order.getId());
        List<StockDeductDTO> stockRecoverList = items.stream()
                .map(item -> new StockDeductDTO(item.getGoodsId(), item.getCount()))
                .collect(Collectors.toList());
        goodsMapper.recoverStock(stockRecoverList);

        order.setOrderStatus((byte)-2);
        order.setOrderNo(orderNo);
        order.setExtraInfo("用户取消订单");
        order.setUpdateTime(LocalDateTime.now());
        orderMapper.update(order);
    }

    @SneakyThrows
    @Override
    public void confirm(String orderNo) {
        Long userId=TLUtils.getUserId();
        Order order=orderMapper.getByOrderNo(orderNo);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        if (!order.getUserId().equals(userId)) {
            throw new RuntimeException("无权操作");
        }
        if(order.getOrderStatus()!=OrderStatusEnum.ORDER_EXPRESS.getStatus()){
            throw new Exception("订单状态错误,无法确认");
        }
        order.setOrderStatus((byte)4);
        order.setOrderNo(orderNo);
        order.setUpdateTime(LocalDateTime.now());
        orderMapper.update(order);
    }

    @Override
    public OrderDetailVO getOrderDetail(String orderNo) {
        Long userId=TLUtils.getUserId();
        Order order=orderMapper.getByOrderNo(orderNo);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        if (!order.getUserId().equals(userId)) {
            throw new RuntimeException("无权操作");
        }
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
        Long userId=TLUtils.getUserId();
        Order order=orderMapper.getByOrderNo(orderPayDTO.getOrderNo());
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        if (!order.getUserId().equals(userId)) {
            throw new RuntimeException("无权操作");
        }
        if (order.getOrderStatus() != OrderStatusEnum.ORDER_PRE_PAY.getStatus()) {
            throw new RuntimeException("订单状态错误，无法支付");
        }
        order.setPayStatus((byte) 1);
        order.setPayType(orderPayDTO.getPayType());
        order.setOrderStatus((byte) OrderStatusEnum.ORDER_PAID.getStatus());
        order.setPayTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());
        orderMapper.update(order);
    }

    @Override
    @Transactional
    public void checkDone(List<Long> orderIds) {
        List<Order> orders = orderMapper.getByIds(orderIds);
        if (orders == null || orders.isEmpty()) {
            throw new RuntimeException("订单不存在");
        }

        for (Order order : orders) {
            if (order.getOrderStatus() != OrderStatusEnum.ORDER_PAID.getStatus()) {
                throw new RuntimeException("订单" + order.getOrderNo() + "状态不是已支付，无法配货");
            }
        }

        orderMapper.setStatus(orderIds, (byte) OrderStatusEnum.ORDER_PACKAGED.getStatus(), LocalDateTime.now());
    }

    @Override
    @Transactional
    public void checkOut(List<Long> orderIds) {
        List<Order> orders = orderMapper.getByIds(orderIds);
        if (orders == null || orders.isEmpty()) {
            throw new RuntimeException("订单不存在");
        }

        for (Order order : orders) {
            if (order.getOrderStatus() != OrderStatusEnum.ORDER_PACKAGED.getStatus()) {
                throw new RuntimeException("订单" + order.getOrderNo() + "状态不是配货完成，无法出库");
            }
        }

        orderMapper.setStatus(orderIds, (byte) OrderStatusEnum.ORDER_EXPRESS.getStatus(), LocalDateTime.now());
    }

    @Override
    @Transactional
    public void closeOrder(List<Long> orderIds) {
        List<Order> orders = orderMapper.getByIds(orderIds);
        if (orders == null || orders.isEmpty()) {
            throw new RuntimeException("订单不存在");
        }

        for (Order order : orders) {
            int status = order.getOrderStatus();
            if (status != OrderStatusEnum.ORDER_PRE_PAY.getStatus() 
                    && status != OrderStatusEnum.ORDER_PAID.getStatus()
                    && status != OrderStatusEnum.ORDER_PACKAGED.getStatus()) {
                throw new RuntimeException("订单" + order.getOrderNo() + "当前状态无法关闭");
            }
        }

        for (Order order : orders) {
            List<OrderItem> items = orderItemMapper.getByOrderId(order.getId());
            List<StockDeductDTO> stockRecoverList = items.stream()
                    .map(item -> new StockDeductDTO(item.getGoodsId(), item.getCount()))
                    .collect(Collectors.toList());
            goodsMapper.recoverStock(stockRecoverList);
        }

        orderMapper.setStatus(orderIds, (byte) OrderStatusEnum.ORDER_CLOSE_BY_ADMIN.getStatus(), LocalDateTime.now());
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
