package my_mall.service.impl;

import java.time.LocalDateTime;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import my_mall.constant.MessageConstant;
import my_mall.entity.dto.CartItemDTO;
import my_mall.entity.dto.ShoppingCartDTO;
import my_mall.entity.dto.ShoppingCartItemDTO;
import my_mall.entity.dto.ShoppingCartPageDTO;
import my_mall.entity.po.Goods;
import my_mall.entity.po.ShoppingCart;
import my_mall.entity.vo.ShoppingCartItemVO;
import my_mall.exception.CartItemNotExistException;
import my_mall.exception.GoodsIsNotSellingException;
import my_mall.exception.GoodsNotExistException;
import my_mall.exception.PowerIsNotEnoughException;
import my_mall.mapper.GoodsMapper;
import my_mall.mapper.ShoppingCartMapper;
import my_mall.result.PageResult;
import my_mall.service.ShoppingCartService;
import my_mall.utils.TLUtils;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Resource
    private ShoppingCartMapper shoppingCartMapper;
    @Resource
    private GoodsMapper goodsMapper;
    @SneakyThrows
    @Override
    public void addItem(CartItemDTO cartItemDTO) {
        Long userId= TLUtils.getUserId();
        Goods goods=goodsMapper.getById(cartItemDTO.getGoodsId());
        if(goods==null){
            throw new GoodsNotExistException(MessageConstant.GOODS_NOT_EXIST + "，商品ID：" + cartItemDTO.getGoodsId() + "，操作用户ID：" + userId);
        }
        if(!goods.getSellStatus()){
            throw new GoodsIsNotSellingException(MessageConstant.GOODS_NOT_SELLING + "，商品ID：" + cartItemDTO.getGoodsId() + "，商品名称：" + goods.getName() + "，操作用户ID：" + userId);
        }

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUserId(userId);
        shoppingCart.setGoodsId(cartItemDTO.getGoodsId());
        shoppingCart.setGoodsCount(cartItemDTO.getGoodsCount());
        shoppingCartMapper.insert(shoppingCart);

    }

    @Override
    public PageResult getPage(ShoppingCartPageDTO pageDTO) {
        PageHelper.startPage(pageDTO.getPageNumber(), pageDTO.getPageSize());
        Long userId= TLUtils.getUserId();
        Page<ShoppingCart> page=shoppingCartMapper.page(pageDTO,userId);
        PageResult pageResult=new PageResult();
        pageResult.setTotal(page.getTotal());
        pageResult.setTotalPage(page.getPages());
        pageResult.setRecords(page.getResult());
        return pageResult;
    }

    @Override
    @Transactional
    public void update(ShoppingCartDTO shoppingCartDTO) {
        Long userId= TLUtils.getUserId();
        ShoppingCart cart = shoppingCartMapper.getById(shoppingCartDTO.getCartItemId());
        if (cart == null) {
            throw new CartItemNotExistException(MessageConstant.CART_ITEM_NOT_EXIST + "，购物车项ID：" + shoppingCartDTO.getCartItemId() + "，操作用户ID：" + userId);
        }
        if (!cart.getUserId().equals(userId)) {
            throw new PowerIsNotEnoughException(MessageConstant.POWER_NOT_ENOUGH + "，购物车项ID：" + shoppingCartDTO.getCartItemId() + "，购物车用户ID：" + cart.getUserId() + "，操作用户ID：" + userId);
        }
        cart.setGoodsCount(shoppingCartDTO.getGoodsCount());
        shoppingCartMapper.update(cart);
    }

    @SneakyThrows
    @Override
    @Transactional
    public void delete(Long cartItemId) {
        Long userId= TLUtils.getUserId();
        ShoppingCart cart = shoppingCartMapper.getById(cartItemId);
        if (cart == null) {
            throw new CartItemNotExistException(MessageConstant.CART_ITEM_NOT_EXIST + "，购物车项ID：" + cartItemId + "，操作用户ID：" + userId);
        }
        if(!Objects.equals(cart.getUserId(), userId)){
            throw new PowerIsNotEnoughException(MessageConstant.POWER_NOT_ENOUGH_CART + "，购物车项ID：" + cartItemId + "，购物车用户ID：" + cart.getUserId() + "，操作用户ID：" + userId);
        }
        shoppingCartMapper.deleteById(cartItemId);
    }

    @Override
    public ShoppingCartItemVO getCartItem(ShoppingCartItemDTO cartItemDTO) {
        Long cartItemId=cartItemDTO.getCartItemId();
        return shoppingCartMapper.getCartItem(cartItemId);
    }
}
