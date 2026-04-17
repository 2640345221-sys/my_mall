package my_mall.mapper;

import my_mall.entity.po.GoodsCategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CategoryMapper {
    @Select("select * from my_mall.goods_category order by parent_id asc,id asc")
    List<GoodsCategory> getAll();

    void insert(GoodsCategory category);

    void deleteBatch(List<Long> ids);

    void update(GoodsCategory category);
    @Select("select * from my_mall.goods_category where id=#{id}")
    GoodsCategory getById(Long id);
}
