package my_mall.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.github.pagehelper.Page;

import my_mall.annotation.OperationFill;
import my_mall.entity.dto.OrderCartDTO;
import my_mall.entity.dto.ShoppingCartPageDTO;
import my_mall.entity.po.ShoppingCart;
import my_mall.entity.vo.ShoppingCartItemVO;

@Mapper
//购物车表的增删改查
public interface ShoppingCartMapper {
    @OperationFill(fillCreateTime = true,fillUpdateTime = true)
    void insert(ShoppingCart shoppingCart);

    Page<ShoppingCartItemVO> page(ShoppingCartPageDTO pageDTO, Long userId);
    @OperationFill(fillUpdateTime = true)
    void update(ShoppingCart shoppingCart);

    void deleteById(Long cartItemId);

    ShoppingCartItemVO getCartItem(Long cartItemId);

    //查购物车项并带上商品信息
    List<OrderCartDTO> getWithGoods(List<Long> cartItemIds, Long userId);

    //批量删除购物车项
    void deleteBatch(List<Long> cartItemIds,  Long userId);
    @Select("select * from my_mall.shopping_cart where id=#{id}")
    ShoppingCart getById(Long cartItemId);

    //查销量最高的商品id（热销榜）
    List<Long> selectTopSellingGoodsIds(Integer limit);

    //按商品id删除购物车项（商品下架时清购物车）
    void deleteByGoodsId(Long goodsId);

    //按用户和商品查购物车项（判断是否已加过）
    @Select("select * from my_mall.shopping_cart where user_id = #{userId} and goods_id = #{goodsId}")
    ShoppingCart getByUserIdAndGoodsId(@Param("userId") Long userId, @Param("goodsId") Long goodsId);
}
