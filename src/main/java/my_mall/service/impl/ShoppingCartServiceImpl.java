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
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(shoppingCartDTO.getCartItemId());
        shoppingCart.setGoodsCount(shoppingCartDTO.getGoodsCount());
        shoppingCart.setUpdateTime(LocalDateTime.now());
        shoppingCartMapper.update(shoppingCart);
    }

    @Override
    public void delete(Long cartItemId) {
        shoppingCartMapper.deleteById(cartItemId);
    }

    @Override
    public ShoppingCartItemVO getCartItem(ShoppingCartItemDTO cartItemDTO) {
        Long cartItemId=cartItemDTO.getCartItemId();
        return shoppingCartMapper.getCartItem(cartItemId);
    }
}
