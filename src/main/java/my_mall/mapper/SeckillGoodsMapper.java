package my_mall.mapper;

import com.github.pagehelper.Page;
import my_mall.entity.dto.SeckillGoodsPageDTO;
import my_mall.entity.po.SeckillGoods;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface SeckillGoodsMapper {

    void insert(SeckillGoods seckillGoods);

    void update(SeckillGoods seckillGoods);

    void deleteById(Long id);

    void deleteByGoodsId(Long goodsId);

    SeckillGoods getById(Long id);

    Page<SeckillGoods> pageQuery(SeckillGoodsPageDTO dto);

    void updateStatus(@Param("id") Long id, @Param("status") Integer status);
    @Select("select * from my_mall.seckill_goods where start_time<=Now() and end_time>=Now()")
    List<SeckillGoods> selectActiveList();

    @Update("update my_mall.seckill_goods set stock_count =stock_count-#{count} where stock_count>=#{count} and id=#{id}")
    int decreaseStock(@Param("id")Long id,@Param("count") Integer count);
}