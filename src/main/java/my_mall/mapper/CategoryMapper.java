package my_mall.mapper;

import my_mall.annotation.OperationFill;
import my_mall.entity.po.GoodsCategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
//商品分类表的增删改查
public interface CategoryMapper {
    @Select("select * from my_mall.goods_category order by parent_id asc,id asc")
    //查所有分类（按父分类和id排序，用于构建分类树）
    List<GoodsCategory> getAll();

    @OperationFill(fillUpdateTime = true,fillUpdateUser = true,fillCreateUser = true,fillCreateTime = true)
    void insert(GoodsCategory category);

    //批量删除分类
    void deleteBatch(List<Long> ids);
    @OperationFill(fillUpdateTime = true,fillUpdateUser = true)
    void update(GoodsCategory category);
    @Select("select * from my_mall.goods_category where id=#{id}")
    GoodsCategory getById(Long id);
}
