package my_mall.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import jakarta.annotation.Resource;
import my_mall.utils.IdGenerator;
import lombok.SneakyThrows;
import my_mall.constant.MessageConstant;
import my_mall.entity.dto.OrderCartDTO;
import my_mall.entity.dto.OrderDTO;
import my_mall.entity.dto.OrderPageDTO;
import my_mall.entity.dto.OrderPayDTO;
import my_mall.entity.dto.StockDeductDTO;
import my_mall.entity.po.Goods;
import my_mall.entity.po.Order;
import my_mall.entity.po.OrderAddress;
import my_mall.entity.po.OrderItem;
import my_mall.entity.po.UserAddress;
import my_mall.entity.vo.OrderDetailVO;
import my_mall.enums.OrderPayStatusEnum;
import my_mall.enums.OrderPayTypeEnum;
import my_mall.enums.OrderStatusEnum;
import my_mall.exception.AddressNotExistException;
import my_mall.exception.BaseException;
import my_mall.exception.CartItemNotExistException;
import my_mall.exception.GoodsIsNotSellingException;
import my_mall.exception.GoodsNotExistException;
import my_mall.exception.OrderNotExistException;
import my_mall.exception.PowerIsNotEnoughException;
import my_mall.exception.StockNumNotEnoughException;
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
    @Resource
    private IdGenerator idGenerator;
    @SneakyThrows
    @Override
    @Transactional
    public void save(OrderDTO orderDTO) {
        Long addressId = orderDTO.getAddressId();
        List<Long> cartItemIds = orderDTO.getCartItemIds();
        Long userId = TLUtils.getUserId();
        UserAddress userAddress =addressMapper.getAddressById(addressId);
        if(userAddress==null||!userAddress.getUserId().equals(userId)){
            throw new AddressNotExistException(MessageConstant.ADDRESS_NOT_EXIST + "，地址ID：" + addressId + "，操作用户ID：" + userId);
        }
        List<OrderCartDTO> cartList=shoppingCartMapper.getWithGoods(cartItemIds, userId);
        if(cartList==null|| cartList.isEmpty()){
            throw new CartItemNotExistException(MessageConstant.CART_EMPTY + "，购物车项ID：" + cartItemIds + "，操作用户ID：" + userId);
        }
        List<StockDeductDTO> list = new ArrayList<>();

        Integer totalPrice = 0;
        for (OrderCartDTO cartItem : cartList) {
            Goods goods = goodsMapper.getById(cartItem.getGoodsId());
            if (goods == null) {
                throw new GoodsNotExistException(MessageConstant.GOODS_NOT_EXIST + "，商品ID：" + cartItem.getGoodsId() + "，操作用户ID：" + userId);
            }
            if (goods.getSellStatus()) {
                throw new GoodsIsNotSellingException(MessageConstant.GOODS_NOT_SELLING + "，商品ID：" + cartItem.getGoodsId() + "，商品名称：" + cartItem.getGoodsName() + "，操作用户ID：" + userId);
            }
            if (cartItem.getCount() > goods.getStockNum()) {
                throw new StockNumNotEnoughException(MessageConstant.STOCK_NOT_ENOUGH + "，商品ID：" + cartItem.getGoodsId() + "，商品名称：" + cartItem.getGoodsName() + "，库存：" + goods.getStockNum() + "，操作用户ID：" + userId);
            }
            totalPrice += cartItem.getCount() * goods.getSellingPrice();
            cartItem.setPrice(goods.getSellingPrice());
            list.add(new StockDeductDTO(goods.getId(), cartItem.getCount()));
        }

        goodsMapper.deductStock(list);

        Order order = new Order();
        order.setUserId(userId);
        order.setTotalPrice(totalPrice);
        order.setPayStatus(OrderPayStatusEnum.NO_PAY.getValue());
        order.setOrderStatus(OrderStatusEnum.ORDER_PRE_PAY.getStatus());
        order.setPayType(OrderPayTypeEnum.NO_PAY.getValue());
        order.setExtraInfo("");
        order.setOrderNo(idGenerator.generateOrderNo());
        orderMapper.insert(order);

        List<OrderItem> orderItemList = cartList.stream().map(x -> {
            OrderItem orderItem = new OrderItem();
            BeanUtils.copyProperties(x, orderItem);
            orderItem.setCreateTime(LocalDateTime.now());
            orderItem.setOrderId(order.getId());
            return orderItem;
        }).collect(Collectors.toList());
        orderItemMapper.insertBatch(orderItemList);

        OrderAddress orderAddress=new OrderAddress();
        BeanUtils.copyProperties(userAddress,orderAddress);
        orderAddress.setOrderId(order.getId());
        orderAddressMapper.insert(orderAddress);

        shoppingCartMapper.deleteBatch(cartItemIds, userId);
    }

    @SneakyThrows
    @Override
    @Transactional
    public void cancel(String orderNo) {
        Long userId=TLUtils.getUserId();
        Order order=orderMapper.getByOrderNo(orderNo);
        if(order==null){
            throw new OrderNotExistException(MessageConstant.ORDER_NOT_EXIST + "，订单号：" + orderNo + "，操作用户ID：" + userId);
        }
        if(!order.getUserId().equals(userId)){
            throw new PowerIsNotEnoughException(MessageConstant.POWER_NOT_ENOUGH_ORDER + "，订单号：" + orderNo + "，订单用户ID：" + order.getUserId() + "，操作用户ID：" + userId);
        }
        if(!OrderStatusEnum.canCancel(order.getOrderStatus())){
            throw new BaseException(MessageConstant.ORDER_CANNOT_CANCEL);
        }

        List<OrderItem> items = orderItemMapper.getByOrderId(order.getId());
        List<StockDeductDTO> stockRecoverList = items.stream()
                .map(item -> new StockDeductDTO(item.getGoodsId(), item.getCount()))
                .collect(Collectors.toList());
        goodsMapper.recoverStock(stockRecoverList);

        order.setOrderStatus(OrderStatusEnum.ORDER_CLOSE_CONFIRM.getStatus());
        order.setOrderNo(orderNo);
        order.setExtraInfo("用户取消订单");
        orderMapper.update(order);
    }

    @SneakyThrows
    @Override
    @Transactional
    public void confirm(String orderNo) {
        Long userId=TLUtils.getUserId();
        Order order=orderMapper.getByOrderNo(orderNo);
        if (order == null) {
            throw new OrderNotExistException(MessageConstant.ORDER_NOT_EXIST + "，订单号：" + orderNo + "，操作用户ID：" + userId);
        }
        if (!order.getUserId().equals(userId)) {
            throw new PowerIsNotEnoughException(MessageConstant.POWER_NOT_ENOUGH + "，订单号：" + orderNo + "，订单用户ID：" + order.getUserId() + "，操作用户ID：" + userId);
        }
        if(!Objects.equals(order.getOrderStatus(), OrderStatusEnum.ORDER_EXPRESS.getStatus())){
            throw new BaseException(MessageConstant.ORDER_STATUS_ERROR + ",无法确认");
        }
        order.setOrderStatus((OrderStatusEnum.ORDER_SUCCESS.getStatus()));
        order.setOrderNo(orderNo);
        orderMapper.update(order);
    }

    @Override
    public OrderDetailVO getOrderDetail(String orderNo) {
        Long userId = TLUtils.getUserId();
        Order order = orderMapper.getByOrderNo(orderNo);
        if (order == null) {
            throw new OrderNotExistException(MessageConstant.ORDER_NOT_EXIST + "，订单号：" + orderNo + "，操作用户ID：" + userId);
        }
        if (!order.getUserId().equals(userId)) {
            throw new PowerIsNotEnoughException(MessageConstant.POWER_NOT_ENOUGH + "，订单号：" + orderNo + "，订单用户ID：" + order.getUserId() + "，操作用户ID：" + userId);
        }
        return toDetailVO(order);
    }

    @Override
    public PageResult getPage(OrderPageDTO orderPageDTO) {
        Long userId = TLUtils.getUserId();
        PageHelper.startPage(orderPageDTO.getPageNumber(), orderPageDTO.getPageSize());
        Page<Order> page = orderMapper.getByUserId(orderPageDTO, userId);
        return buildPageResult(page);
    }

    @Override
    @Transactional
    public void paySuccess(OrderPayDTO orderPayDTO) {
        Long userId=TLUtils.getUserId();
        Order order=orderMapper.getByOrderNo(orderPayDTO.getOrderNo());
        if (order == null) {
            throw new OrderNotExistException("订单不存在");
        }
        if (!order.getUserId().equals(userId)) {
            throw new PowerIsNotEnoughException("无权操作");
        }
        if (!Objects.equals(order.getOrderStatus(), OrderStatusEnum.ORDER_PRE_PAY.getStatus())) {
            throw new BaseException(MessageConstant.ORDER_CANNOT_PAY);
        }
        order.setPayStatus(OrderPayStatusEnum.PAID.getValue());
        order.setPayType(orderPayDTO.getPayType());
        order.setOrderStatus(OrderStatusEnum.ORDER_PAID.getStatus());
        order.setPayTime(LocalDateTime.now());
        orderMapper.update(order);
    }

    @Override
    @Transactional
    public void checkDone(List<Long> orderIds) {
        List<Order> orders = orderMapper.getByIds(orderIds);
        if (orders == null || orders.isEmpty()) {
            throw new OrderNotExistException(MessageConstant.ORDER_NOT_EXIST + "，订单ID列表：" + orderIds + "，操作用户ID：" + TLUtils.getUserId());
        }

        for (Order order : orders) {
            if (!Objects.equals(order.getOrderStatus(), OrderStatusEnum.ORDER_PAID.getStatus())) {
                throw new BaseException("订单" + order.getOrderNo() + MessageConstant.ORDER_CANNOT_CHECK);
            }
        }

        orderMapper.setStatus(orderIds, OrderStatusEnum.ORDER_PACKAGED.getStatus(), LocalDateTime.now());
    }

    @Override
    @Transactional
    public void checkOut(List<Long> orderIds) {
        List<Order> orders = orderMapper.getByIds(orderIds);
        if (orders == null || orders.isEmpty()) {
            throw new OrderNotExistException(MessageConstant.ORDER_NOT_EXIST + "，订单ID列表：" + orderIds + "，操作用户ID：" + TLUtils.getUserId());
        }

        for (Order order : orders) {
            if (!Objects.equals(order.getOrderStatus(), OrderStatusEnum.ORDER_PACKAGED.getStatus())) {
                throw new BaseException("订单" + order.getOrderNo() + MessageConstant.ORDER_CANNOT_CHECKOUT);
            }
        }

        orderMapper.setStatus(orderIds,  OrderStatusEnum.ORDER_EXPRESS.getStatus(), LocalDateTime.now());
    }

    @Override
    @Transactional
    public void closeOrder(List<Long> orderIds) {
        List<Order> orders = orderMapper.getByIds(orderIds);
        if (orders == null || orders.isEmpty()) {
            throw new OrderNotExistException(MessageConstant.ORDER_NOT_EXIST + "，订单ID列表：" + orderIds + "，操作用户ID：" + TLUtils.getUserId());
        }

        for (Order order : orders) {
            int status = order.getOrderStatus();
            if (status != OrderStatusEnum.ORDER_PRE_PAY.getStatus() 
                    && status != OrderStatusEnum.ORDER_PAID.getStatus()
                    && status != OrderStatusEnum.ORDER_PACKAGED.getStatus()) {
                throw new BaseException("订单" + order.getOrderNo() + MessageConstant.ORDER_CANNOT_CLOSE);
            }
            List<OrderItem> items = orderItemMapper.getByOrderId(order.getId());
            List<StockDeductDTO> stockRecoverList = items.stream()
                    .map(item -> new StockDeductDTO(item.getGoodsId(), item.getCount()))
                    .collect(Collectors.toList());
            goodsMapper.recoverStock(stockRecoverList);
        }

        orderMapper.setStatus(orderIds, OrderStatusEnum.ORDER_CLOSE_CANCEL.getStatus(), LocalDateTime.now());
    }

    @Override
    public PageResult aGetPage(OrderPageDTO orderPageDTO) {
        PageHelper.startPage(orderPageDTO.getPageNumber(), orderPageDTO.getPageSize());
        Page<Order> page = orderMapper.aGetByUserId(orderPageDTO);
        return buildPageResult(page);
    }

    private OrderDetailVO toDetailVO(Order order) {
        List<OrderItem> items = orderItemMapper.getByOrderId(order.getId());
        List<OrderCartDTO> cartDTOs = items.stream().map(item -> {
            OrderCartDTO dto = new OrderCartDTO();
            BeanUtils.copyProperties(item, dto);
            return dto;
        }).collect(Collectors.toList());

        OrderDetailVO vo = new OrderDetailVO();
        BeanUtils.copyProperties(order, vo);
        vo.setOrderCartDTO(cartDTOs);
        vo.setOrderAddress(orderAddressMapper.getByOrderId(order.getId()));
        return vo;
    }

    private PageResult buildPageResult(Page<Order> page) {
        List<Long> ids = page.getResult().stream().map(Order::getId).collect(Collectors.toList());
        List<OrderItem> items = orderItemMapper.getBatchByOrderId(ids);
        Map<Long, List<OrderItem>> itemMap = items.stream()
                .collect(Collectors.groupingBy(OrderItem::getOrderId));

        List<OrderDetailVO> detailVOList = page.getResult().stream().map(order -> {
            OrderDetailVO vo = new OrderDetailVO();
            BeanUtils.copyProperties(order, vo);
            List<OrderItem> orderItems = itemMap.getOrDefault(order.getId(), Collections.emptyList());
            List<OrderCartDTO> cartDTOs = orderItems.stream().map(item -> {
                OrderCartDTO dto = new OrderCartDTO();
                BeanUtils.copyProperties(item, dto);
                return dto;
            }).collect(Collectors.toList());
            vo.setOrderCartDTO(cartDTOs);
            vo.setOrderAddress(orderAddressMapper.getByOrderId(order.getId()));
            return vo;
        }).collect(Collectors.toList());

        PageResult pageResult = new PageResult();
        pageResult.setTotal(page.getTotal());
        pageResult.setTotalPage(page.getPages());
        pageResult.setRecords(detailVOList);
        return pageResult;
    }

    @Override
    public OrderDetailVO aGetOrderDetail(String orderNo) {
        Order order = orderMapper.getByOrderNo(orderNo);
        if (order == null) {
            throw new OrderNotExistException(MessageConstant.ORDER_NOT_EXIST + "，订单号：" + orderNo);
        }
        return toDetailVO(order);
    }

}
