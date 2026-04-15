package my_mall.service;

import my_mall.entity.dto.CartItemDTO;
import my_mall.entity.dto.ShoppingCartDTO;
import my_mall.entity.dto.ShoppingCartItemDTO;
import my_mall.entity.dto.ShoppingCartPageDTO;
import my_mall.entity.vo.ShoppingCartItemVO;
import my_mall.result.PageResult;

public interface ShoppingCartService {
    void addItem(CartItemDTO cartItemDTO);

    PageResult getPage(ShoppingCartPageDTO pageDTO);

    void update(ShoppingCartDTO cartItemDTO);

    void delete(Long cartItemId);

    ShoppingCartItemVO getCartItem(ShoppingCartItemDTO cartItemDTO);
}
