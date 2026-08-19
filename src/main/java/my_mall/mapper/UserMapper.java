package my_mall.mapper;

import com.github.pagehelper.Page;
import my_mall.annotation.OperationFill;
import my_mall.entity.dto.UserPageDTO;
import my_mall.entity.po.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
//用户表的增删改查
public interface UserMapper {

    @Select("select * from my_mall.user where login_name=#{username}")
    User getByUsername(String username);
    
    @OperationFill(fillCreateTime = true)
    void insert(User user);

    @Select("select * from my_mall.user where id=#{userId}")
    User getById(Long userId);

    void update(User user);

    Page<User> getPage(UserPageDTO userPageDTO);

    //批量修改用户锁定状态
    void setStatus(Integer lockStatus, List<Long> ids);

    //按登录名查用户
    User getByLoginName(String username);
}
