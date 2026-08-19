package my_mall.mapper;

import java.util.List;

import my_mall.annotation.OperationFill;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.github.pagehelper.Page;

import my_mall.entity.dto.IndexPageDTO;
import my_mall.entity.po.IndexConfig;

@Mapper
//首页配置表的增删改查
public interface IndexConfigMapper {
    Page<IndexConfig> getPage(IndexPageDTO indexPageDTO);

    @Select("select * from my_mall.index_config where id=#{id}")
    IndexConfig getById(Long id);

    void deleteBatch(List<Long> ids);

    @OperationFill(fillUpdateTime = true,fillUpdateUser = true)
    void update(IndexConfig indexConfig);

    @OperationFill(fillUpdateTime = true,fillUpdateUser = true,fillCreateTime = true,fillCreateUser = true)
    void insert(IndexConfig indexConfig);

    //按类型查首页配置（新品/热销/推荐）
    Page<IndexConfig> getByType(Integer type);
    
    @Select("delete from my_mall.index_config where type = #{type}")
    //按类型删除首页配置
    void deleteByType(Integer type);
    
    //批量新增首页配置
    void insertBatch(List<IndexConfig> list);

    //按商品id删除首页配置（商品下架时清理）
    void deleteByGoodsId(Long goodsId);
}
