package my_mall.mapper;

import java.util.List;

import my_mall.annotation.OperationFill;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.github.pagehelper.Page;

import my_mall.entity.dto.IndexPageDTO;
import my_mall.entity.po.IndexConfig;

@Mapper
public interface IndexConfigMapper {
    Page<IndexConfig> getPage(IndexPageDTO indexPageDTO);

    @Select("select * from my_mall.index_config where id=#{id}")
    IndexConfig getById(Long id);

    void deleteBatch(List<Long> ids);

    @OperationFill(fillUpdateTime = true,fillUpdateUser = true)
    void update(IndexConfig indexConfig);

    @OperationFill(fillUpdateTime = true,fillUpdateUser = true,fillCreateTime = true,fillCreateUser = true)
    void insert(IndexConfig indexConfig);

    Page<IndexConfig> getByType(Integer type);
    
    @Select("delete from my_mall.index_config where type = #{type}")
    void deleteByType(Integer type);
    
    void insertBatch(List<IndexConfig> list);
}
