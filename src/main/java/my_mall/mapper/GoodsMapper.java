package my_mall.mapper;

import java.util.List;

import my_mall.annotation.OperationFill;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.github.pagehelper.Page;

import my_mall.entity.dto.GoodsPageDTO;
import my_mall.entity.dto.GoodsPageSearchDTO;
import my_mall.entity.dto.StockDeductDTO;
import my_mall.entity.po.Goods;

@Mapper
//商品表的增删改查
public interface GoodsMapper {
    @Select("select * from my_mall.goods where id=#{goodsId}")
    Goods getById(Long goodsId);

    Page<Goods> getPage(GoodsPageSearchDTO goodsPageSearchDTO);

    @OperationFill(fillUpdateTime = true,fillUpdateUser = true,fillCreateUser = true,fillCreateTime = true)
    void insert(Goods goods);

    Page<Goods> page(GoodsPageDTO goodsPageDTO);
    @OperationFill(fillUpdateTime = true,fillUpdateUser = true)
    //批量修改商品上下架状态
    void updateStatus(Integer sellStatus, List<Long> ids);

    @OperationFill(fillUpdateTime = true,fillUpdateUser = true)
    void updateGoods(Goods goods);

    //批量扣减库存
    int deductStock(List<StockDeductDTO> list);

    //单个扣减库存（带库存充足校验）
    int decreaseStock(@Param("id") Long id, @Param("count") Integer count);

    //恢复库存（订单取消时回补）
    void recoverStock(List<StockDeductDTO> stockList);
    @Select("select * from my_mall.goods where category_id=#{categoryId} and name=#{name}")
    //按分类和名字查商品（用于查重）
    Goods getByCategoryAndName(Long categoryId, String name);

    //按id列表批量查商品
    List<Goods> getByIdBatch(@Param("ids")List<Long> ids);

    void deleteBatch(Long id);
    
    @Select("select * from my_mall.goods where sell_status = 0 order by create_time desc limit #{limit}")
    //查最新上架的商品（首页新品栏）
    List<Goods> getLatestGoods(int limit);
    
    @Select("select * from my_mall.goods where sell_status = 0 order by stock_num desc limit #{limit}")
    //查热销商品（按库存排序）
    List<Goods> getHotGoods(int limit);

}
