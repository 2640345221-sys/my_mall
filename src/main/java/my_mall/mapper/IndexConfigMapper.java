package my_mall.mapper;

import com.github.pagehelper.Page;
import my_mall.entity.dto.IndexPageDTO;
import my_mall.entity.po.IndexConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface IndexConfigMapper {
    Page<IndexConfig> getPage(IndexPageDTO indexPageDTO);

    @Select("select * from my_mall.index_config where id=#{id}")
    IndexConfig getById(Long id);

    void deleteBatch(List<Long> ids);

    void update(IndexConfig indexConfig);

    void insert(IndexConfig indexConfig);
}
