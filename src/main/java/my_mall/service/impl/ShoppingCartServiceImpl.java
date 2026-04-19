package my_mall.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import my_mall.entity.dto.CartItemDTO;
import my_mall.entity.dto.ShoppingCartDTO;
import my_mall.entity.dto.ShoppingCartItemDTO;
import my_mall.entity.dto.ShoppingCartPageDTO;
import my_mall.entity.po.Goods;
import my_mall.entity.po.OrderItem;
import my_mall.entity.po.ShoppingCart;
import my_mall.entity.vo.ShoppingCartItemVO;
import my_mall.mapper.GoodsMapper;
import my_mall.mapper.ShoppingCartMapper;
import my_mall.result.PageResult;
import my_mall.service.ShoppingCartService;
import my_mall.utils.TLUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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
        if(userId==null){
            throw new  Exception("用户未登录");
        }
        Goods goods=goodsMapper.getById(cartItemDTO.getGoodsId());
        if(goods==null){
            throw new Exception("商品不存在"+goods.getId());
        }
        if(!goods.getSellStatus()){
            throw new Exception("商品已下架"+goods.getName());
        }

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUserId(userId);
        shoppingCart.setGoodsId(cartItemDTO.getGoodsId());
        shoppingCart.setGoodsCount(cartItemDTO.getGoodsCount());
        shoppingCart.setCreateTime(LocalDateTime.now());
        shoppingCart.setUpdateTime(LocalDateTime.now());
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
    public void update(ShoppingCartDTO shoppingCartDTO) {
        Long userId= TLUtils.getUserId();
        ShoppingCart cart = shoppingCartMapper.getById(shoppingCartDTO.getCartItemId());
        if (cart == null) {
            throw new RuntimeException("购物车项不存在");
        }
        if (!cart.getUserId().equals(userId)) {
            throw new RuntimeException("无权操作");
        }
        if (shoppingCartDTO.getGoodsCount() < 1 || shoppingCartDTO.getGoodsCount() > 5) {
            throw new RuntimeException("数量超出限制");
        }
        cart.setGoodsCount(shoppingCartDTO.getGoodsCount());
        cart.setUpdateTime(LocalDateTime.now());
        shoppingCartMapper.update(cart);
    }

    @SneakyThrows
    @Override
    public void delete(Long cartItemId) {
        Long userId= TLUtils.getUserId();
        ShoppingCart cart = shoppingCartMapper.getById(cartItemId);
        if (cart == null) {
            throw new Exception("订单不存在");
        }
        if(cart.getUserId()!=userId){
            throw new Exception("修改权限不足");
        }
        shoppingCartMapper.deleteById(cartItemId);
    }

    @Override
    public ShoppingCartItemVO getCartItem(ShoppingCartItemDTO cartItemDTO) {
        Long cartItemId=cartItemDTO.getCartItemId();
        return shoppingCartMapper.getCartItem(cartItemId);
    }
}
