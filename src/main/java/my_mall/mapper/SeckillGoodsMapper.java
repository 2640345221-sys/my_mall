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
//秒杀商品表的增删改查
public interface SeckillGoodsMapper {

    void insert(SeckillGoods seckillGoods);

    void update(SeckillGoods seckillGoods);

    void deleteById(Long id);

    void deleteByGoodsId(Long goodsId);

    SeckillGoods getById(Long id);

    Page<SeckillGoods> pageQuery(SeckillGoodsPageDTO dto);

    //修改秒杀商品状态
    void updateStatus(@Param("id") Long id, @Param("status") Integer status);
    //查进行中的秒杀商品
    @Select("select * from my_mall.seckill_goods where start_time<=Now() and end_time>=Now()")
    List<SeckillGoods> selectActiveList();

    //查到开始时间但还没启用的秒杀商品
    @Select("select * from my_mall.seckill_goods where start_time<=Now() and status=0")
    List<SeckillGoods> selectToStart();

    //查到结束时间但还启用的秒杀商品
    @Select("select * from my_mall.seckill_goods where end_time<Now() and status=1")
    List<SeckillGoods> selectToEnd();

    @Update("update my_mall.seckill_goods set stock_count =stock_count-#{count} where stock_count>=#{count} and id=#{id}")
    //扣减秒杀库存（带库存充足校验）
    int decreaseStock(@Param("id")Long id,@Param("count") Integer count);
}