package my_mall.mapper;

import my_mall.entity.po.Admin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
//管理员表的增删改查
public interface AdminMapper {
    @Select("select * from my_mall.admin where username=#{username}")
    //按用户名查管理员
    Admin getByUsername(String username);

    void update(Admin admin);

    Admin getById(Long id);
}
