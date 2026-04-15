package my_mall.mapper;

import my_mall.entity.po.GoodsCategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CategoryMapper {
    @Select("select * from my_mall.goods_category order by parent_id asc,id asc")
    List<GoodsCategory> getAll();
}
