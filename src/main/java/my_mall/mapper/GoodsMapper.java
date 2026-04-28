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
public interface GoodsMapper {
    @Select("select * from my_mall.goods where id=#{goodsId}")
    Goods getById(Long goodsId);

    Page<Goods> getPage(GoodsPageSearchDTO goodsPageSearchDTO);

    @OperationFill(fillUpdateTime = true,fillUpdateUser = true,fillCreateUser = true,fillCreateTime = true)
    void insert(Goods goods);

    Page<Goods> page(GoodsPageDTO goodsPageDTO);

    void updateStatus(Integer sellStatus, List<Long> ids);

    @OperationFill(fillUpdateTime = true,fillUpdateUser = true)
    void updateGoods(Goods goods);

    void deductStock(List<StockDeductDTO> list);

    void recoverStock(List<StockDeductDTO> stockList);
    @Select("select * from my_mall.goods where category_id=#{categoryId} and name=#{name}")
    Goods getByCategoryAndName(Long categoryId, String name);

    List<Goods> getByIdBatch(@Param("ids")List<Long> ids);

    void deleteBatch(Long id);
    
    @Select("select * from my_mall.goods where sell_status = 0 order by create_time desc limit #{limit}")
    List<Goods> getLatestGoods(int limit);
    
    @Select("select * from my_mall.goods where sell_status = 0 order by stock_num desc limit #{limit}")
    List<Goods> getHotGoods(int limit);

}
