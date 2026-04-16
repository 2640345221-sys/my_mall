package my_mall.mapper;

import my_mall.entity.po.Admin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AdminMapper {
    @Select("select * from my_mall.admin where username=#{username}")
    Admin getByUsername(String username);

    void update(Admin admin);

    Admin getById(Long id);
}
