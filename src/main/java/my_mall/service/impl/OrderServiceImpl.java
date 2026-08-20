package my_mall.service.impl;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import jakarta.annotation.Resource;
import my_mall.utils.IdGenerator;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
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
import my_mall.entity.po.SeckillOrder;
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
import my_mall.mapper.SeckillGoodsMapper;
import my_mall.mapper.SeckillOrderMapper;
import my_mall.mapper.ShoppingCartMapper;
import my_mall.result.PageResult;
import my_mall.service.OrderService;
import my_mall.utils.TLUtils;

@Service
@Slf4j
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
    private SeckillOrderMapper seckillOrderMapper;
    @Resource
    private SeckillGoodsMapper seckillGoodsMapper;
    @Resource(name = "stringRedisTemplate")
    private StringRedisTemplate redisTemplate;
    @Resource
    private IdGenerator idGenerator;
    @SneakyThrows
    @Override
    @Transactional
    //创建订单：校验地址和购物车 → 扣库存算总价 → 写订单/订单项/地址 → 清购物车
    public String save(OrderDTO orderDTO) {
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
        Integer totalPrice = 0;
        for (OrderCartDTO cartItem : cartList) {
            Goods goods = goodsMapper.getById(cartItem.getGoodsId());
            if (goods == null) {
                throw new GoodsNotExistException(MessageConstant.GOODS_NOT_EXIST + "，商品ID：" + cartItem.getGoodsId() + "，操作用户ID：" + userId);
            }
            if (goods.getSellStatus()) {
                throw new GoodsIsNotSellingException(MessageConstant.GOODS_NOT_SELLING + "，商品ID：" + cartItem.getGoodsId() + "，商品名称：" + cartItem.getGoodsName() + "，操作用户ID：" + userId);
            }
            //扣减库存，rows=0 表示库存不足
            int rows = goodsMapper.decreaseStock(goods.getId(), cartItem.getCount());
            if (rows == 0) {
                throw new StockNumNotEnoughException(MessageConstant.STOCK_NOT_ENOUGH + "，商品ID：" + cartItem.getGoodsId() + "，商品名称：" + cartItem.getGoodsName() + "，操作用户ID：" + userId);
            }
            totalPrice += cartItem.getCount() * goods.getSellingPrice();
            cartItem.setPrice(goods.getSellingPrice());
        }

        Order order = new Order();
        order.setUserId(userId);
        order.setTotalPrice(totalPrice);
        order.setPayStatus(OrderPayStatusEnum.NO_PAY.getValue());
        order.setOrderStatus(OrderStatusEnum.ORDER_PRE_PAY.getStatus());
        order.setPayType(OrderPayTypeEnum.NO_PAY.getValue());
        order.setExtraInfo("");
        //雪花id生成唯一订单号
        order.setOrderNo(idGenerator.generateOrderNo());
        orderMapper.insert(order);

        //把购物车项转成订单项，批量插入
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
        return order.getOrderNo();
    }

    @SneakyThrows
    @Override
    @Transactional
    //取消订单：校验归属和状态 → 回补库存 → 改为关闭状态
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
        //取消秒杀订单：只恢复秒杀库存，不回补商品库存（商品库存的活动预扣由秒杀结束时的 recoverStockToGoods 统一回补，避免重复回补）
        SeckillOrder seckillOrder = seckillOrderMapper.getByOrderId(order.getId());
        if (seckillOrder != null) {
            if (!items.isEmpty()) {
                int count = items.get(0).getCount();
                seckillGoodsMapper.increaseStock(seckillOrder.getSeckillGoodsId(), count);
                String stockKey = "seckill:stock:" + seckillOrder.getSeckillGoodsId();
                redisTemplate.opsForValue().increment(stockKey, count);
                redisTemplate.expire(stockKey, Duration.ofHours(2));
                //参考普通订单取消状态：取消订单关闭(-3)
                seckillOrderMapper.updateStatus(seckillOrder.getId(), OrderStatusEnum.ORDER_CLOSE_CANCEL.getStatus());
                redisTemplate.delete("seckill:user:" + seckillOrder.getSeckillGoodsId() + ":" + order.getUserId());
                log.info("取消秒杀订单，恢复秒杀库存并标记取消: orderId={}, seckillGoodsId={}, count={}",
                        order.getId(), seckillOrder.getSeckillGoodsId(), count);
            }
        } else {
            //普通订单取消：回补商品库存
            List<StockDeductDTO> stockRecoverList = items.stream()
                    .map(item -> new StockDeductDTO(item.getGoodsId(), item.getCount()))
                    .collect(Collectors.toList());
            goodsMapper.recoverStock(stockRecoverList);
        }

        order.setOrderStatus(OrderStatusEnum.ORDER_CLOSE_CONFIRM.getStatus());
        order.setOrderNo(orderNo);
        order.setExtraInfo("用户取消订单");
        orderMapper.update(order);
    }

    @SneakyThrows
    @Override
    @Transactional
    //确认收货：校验归属和状态 → 改为交易成功
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
    //用户查订单详情（校验订单属于当前用户）
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
    //用户分页查自己的订单
    public PageResult getPage(OrderPageDTO orderPageDTO) {
        Long userId = TLUtils.getUserId();
        PageHelper.startPage(orderPageDTO.getPageNumber(), orderPageDTO.getPageSize());
        Page<Order> page = orderMapper.getByUserId(orderPageDTO, userId);
        return buildPageResult(page);
    }

    @Override
    @Transactional
    //支付成功回调：校验归属和状态 → 改为已支付
    public void paySuccess(OrderPayDTO orderPayDTO) {
        Long userId=TLUtils.getUserId();
        Order order=orderMapper.getByOrderNo(orderPayDTO.getOrderNo());
        if (order == null) {
            throw new OrderNotExistException(MessageConstant.ORDER_NOT_EXIST);
        }
        if (!order.getUserId().equals(userId)) {
            throw new PowerIsNotEnoughException(MessageConstant.POWER_NOT_ENOUGH);
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
    //配货完成（admin）：校验状态为已支付 → 批量改为配货完成
    public void checkDone(List<Long> orderIds) {
        List<Order> orders = orderMapper.getByIds(orderIds);
        if (orders == null || orders.isEmpty()) {
            throw new OrderNotExistException(MessageConstant.ORDER_NOT_EXIST + "，订单ID列表：" + orderIds + "，操作用户ID：" + TLUtils.getUserId());
        }
        if (orders.size() != orderIds.size()) {
            throw new OrderNotExistException(MessageConstant.ORDER_NOT_EXIST + "，部分订单ID不存在，订单ID列表：" + orderIds + "，操作用户ID：" + TLUtils.getUserId());
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
    //出库（admin）：校验状态为配货完成 → 批量改为出库成功
    public void checkOut(List<Long> orderIds) {
        List<Order> orders = orderMapper.getByIds(orderIds);
        if (orders == null || orders.isEmpty()) {
            throw new OrderNotExistException(MessageConstant.ORDER_NOT_EXIST + "，订单ID列表：" + orderIds + "，操作用户ID：" + TLUtils.getUserId());
        }
        if (orders.size() != orderIds.size()) {
            throw new OrderNotExistException(MessageConstant.ORDER_NOT_EXIST + "，部分订单ID不存在，订单ID列表：" + orderIds + "，操作用户ID：" + TLUtils.getUserId());
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
    //关闭订单（admin）：校验状态 → 回补库存 → 批量改为关闭
    public void closeOrder(List<Long> orderIds) {
        List<Order> orders = orderMapper.getByIds(orderIds);
        if (orders == null || orders.isEmpty()) {
            throw new OrderNotExistException(MessageConstant.ORDER_NOT_EXIST + "，订单ID列表：" + orderIds + "，操作用户ID：" + TLUtils.getUserId());
        }
        if (orders.size() != orderIds.size()) {
            throw new OrderNotExistException(MessageConstant.ORDER_NOT_EXIST + "，部分订单ID不存在，订单ID列表：" + orderIds + "，操作用户ID：" + TLUtils.getUserId());
        }

        for (Order order : orders) {
            //只有待支付订单能关闭（未付款，回补库存即可，无需退款）
            if (!Objects.equals(order.getOrderStatus(), OrderStatusEnum.ORDER_PRE_PAY.getStatus())) {
                throw new BaseException("订单" + order.getOrderNo() + MessageConstant.ORDER_CANNOT_CLOSE);
            }
            //关闭订单前回补库存
            List<OrderItem> items = orderItemMapper.getByOrderId(order.getId());
            List<StockDeductDTO> stockRecoverList = items.stream()
                    .map(item -> new StockDeductDTO(item.getGoodsId(), item.getCount()))
                    .collect(Collectors.toList());
            goodsMapper.recoverStock(stockRecoverList);
        }

        orderMapper.setStatus(orderIds, OrderStatusEnum.ORDER_CLOSE_CANCEL.getStatus(), LocalDateTime.now());
    }

    @Override
    //admin 分页查所有用户的订单
    public PageResult aGetPage(OrderPageDTO orderPageDTO) {
        PageHelper.startPage(orderPageDTO.getPageNumber(), orderPageDTO.getPageSize());
        Page<Order> page = orderMapper.aGetByUserId(orderPageDTO);
        return buildPageResult(page);
    }

    //组装一个订单详情：订单 + 订单项列表 + 收货地址
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
    //一批订单详情拼接（订单本身+订单详情+订单地址）
    private PageResult buildPageResult(Page<Order> page) {
        if (page.getResult().isEmpty()) {
            PageResult pageResult = new PageResult();
            pageResult.setTotal(page.getTotal());
            pageResult.setTotalPage(page.getPages());
            pageResult.setRecords(Collections.emptyList());
            return pageResult;
        }
        //批量查订单项和地址，避免逐个订单查询（N+1 问题）
        List<Long> ids = page.getResult().stream().map(Order::getId).collect(Collectors.toList());
        List<OrderItem> items = orderItemMapper.getBatchByOrderId(ids);
        Map<Long, List<OrderItem>> itemMap = items.stream()
                .collect(Collectors.groupingBy(OrderItem::getOrderId));

        Map<Long, OrderAddress> addressMap = orderAddressMapper.getByOrderIds(ids).stream()
                .collect(Collectors.toMap(OrderAddress::getOrderId, a -> a, (a, b) -> a));

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
            vo.setOrderAddress(addressMap.get(order.getId()));
            return vo;
        }).collect(Collectors.toList());

        PageResult pageResult = new PageResult();
        pageResult.setTotal(page.getTotal());
        pageResult.setTotalPage(page.getPages());
        pageResult.setRecords(detailVOList);
        return pageResult;
    }

    @Override
    //admin 查订单详情（不校验归属，可看所有订单）
    public OrderDetailVO aGetOrderDetail(String orderNo) {
        Order order = orderMapper.getByOrderNo(orderNo);
        if (order == null) {
            throw new OrderNotExistException(MessageConstant.ORDER_NOT_EXIST + "，订单号：" + orderNo);
        }
        return toDetailVO(order);
    }
}
