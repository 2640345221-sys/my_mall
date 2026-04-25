package my_mall.mapper;

import com.github.pagehelper.Page;
import my_mall.entity.dto.GoodsPageDTO;
import my_mall.entity.dto.GoodsPageSearchDTO;
import my_mall.entity.dto.StockDeductDTO;
import my_mall.entity.po.Goods;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface GoodsMapper {
    @Select("select * from my_mall.goods where id=#{goodsId}")
    Goods getById(Long goodsId);

    Page<Goods> getPage(GoodsPageSearchDTO goodsPageSearchDTO);

    void insert(Goods goods);

    Page<Goods> page(GoodsPageDTO goodsPageDTO);

    void updateStatus(Integer sellStatus, List<Long> ids);

    void updateGoods(Goods goods);

    void deductStock(List<StockDeductDTO> list);

    void recoverStock(List<StockDeductDTO> stockList);
    @Select("select * from my_mall.goods where category_id=#{categoryId} and name=#{name}")
    Goods getByCategoryAndName(Long categoryId, String name);

    List<Goods> getByIdBatch(List<Long> ids);

    void deleteBatch(Long id);
}
