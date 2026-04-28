package my_mall.mapper;

import java.util.List;

import my_mall.annotation.OperationFill;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.github.pagehelper.Page;

import my_mall.entity.dto.OrderCartDTO;
import my_mall.entity.dto.ShoppingCartPageDTO;
import my_mall.entity.po.ShoppingCart;
import my_mall.entity.vo.ShoppingCartItemVO;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ShoppingCartMapper {
    @OperationFill(fillCreateTime = true,fillUpdateTime = true)
    void insert(ShoppingCart shoppingCart);

    Page<ShoppingCart> page(ShoppingCartPageDTO pageDTO,Long userId);
    @OperationFill(fillUpdateTime = true)
    void update(ShoppingCart shoppingCart);

    void deleteById(Long cartItemId);

    ShoppingCartItemVO getCartItem(Long cartItemId);

    List<OrderCartDTO> getWithGoods(List<Long> cartItemIds, Long userId);

    void deleteBatch(List<Long> cartItemIds,  Long userId);
    @Select("select * from my_mall.shopping_cart where id=#{id}")
    ShoppingCart getById(Long cartItemId);

    List<Long> selectTopSellingGoodsIds(Integer limit);
}
