package my_mall.mapper;

import my_mall.entity.po.User;
import my_mall.entity.po.UserAddress;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {

    @Select("select * from my_mall.user where login_name=#{username}")
    User getByUsername(String username);
    
    @Select("select * from my_mall.user where nick_name=#{nickname}")
    User getByNickName(String nickName);

    void insert(User user);

    @Select("select * from my_mall.user where id=#{userId}")
    User getById(Long userId);

    void update(User user);

}
