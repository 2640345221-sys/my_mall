package my_mall.mapper;

import com.github.pagehelper.Page;
import my_mall.entity.dto.ShoppingCartPageDTO;
import my_mall.entity.po.ShoppingCart;
import my_mall.entity.vo.ShoppingCartItemVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ShoppingCartMapper {
    void insert(ShoppingCart shoppingCart);

    Page<ShoppingCart> page(ShoppingCartPageDTO pageDTO,Long userId);

    void update(ShoppingCart shoppingCart);

    void deleteById(Long cartItemId);

    ShoppingCartItemVO getCartItem(Long cartItemId);
}
