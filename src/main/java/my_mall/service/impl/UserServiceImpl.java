package my_mall.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import my_mall.entity.dto.LoginDTO;
import my_mall.entity.dto.UserPageDTO;
import my_mall.entity.po.User;
import my_mall.entity.vo.UserVO;
import my_mall.mapper.UserMapper;
import my_mall.result.PageResult;
import my_mall.service.UserService;
import my_mall.utils.TLUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImpl  implements UserService {
    @Resource
    private UserMapper userMapper;

    @SneakyThrows
    @Override
    public User login(LoginDTO loginDTO) {
        String username = loginDTO.getUsername();
        User user=userMapper.getByUsername(username);
        if(user==null){
            return null;
        }
        String password = loginDTO.getPassword();
        if(!DigestUtils.md5DigestAsHex(password.getBytes()).equals(user.getPassword())){
            return null;
        }
        if(user.getLocked()==true){
            throw new Exception("用户被锁定");
        }
        return user;
    }

    @SneakyThrows
    @Override
    public void register(LoginDTO loginDTO) {
        //随机生成一个用户名，避免与数据库已有用户冲突
        String nickName = "user" + System.currentTimeMillis();
        while (userMapper.getByNickName(nickName)!=null) {
            nickName = "user" + System.currentTimeMillis() + (int)(Math.random() * 100);
        }
        if(userMapper.getByLoginName(loginDTO.getUsername()) != null){
            throw new Exception("账号已存在");
        }
        if(loginDTO.getPassword()==null){
            throw new Exception("密码为空");
        }
        User user=User.builder()
                .loginName(loginDTO.getUsername())
                .nickName(nickName)
                .password(DigestUtils.md5DigestAsHex(loginDTO.getPassword().getBytes()))
                .locked(false)
                .introduceSign("")
                .createTime(LocalDateTime.now())
                .build();
        userMapper.insert(user);
    }

    @Override
    public User getUserInfo() {
        Long userId= TLUtils.getUserId();
        User user = userMapper.getById(userId);
        System.out.println(user);
        return user;
    }

    @Override
    public void updateUserInfo(UserVO userVO) {
        Long id= TLUtils.getUserId();
        User user =new User();
        BeanUtils.copyProperties(userVO,user);
        user.setId(id);
        if(userVO.getPassword()!=null)
        user.setPassword(DigestUtils.md5DigestAsHex(userVO.getPassword().getBytes()));
        userMapper.update(user);
    }

    @Override
    public PageResult page(UserPageDTO userPageDTO) {
        PageHelper.startPage(userPageDTO.getPageNumber(), userPageDTO.getPageSize());
        Page<User> page=userMapper.getPage(userPageDTO);
        PageResult pageResult=new PageResult();
        pageResult.setTotal(page.getTotal());
        pageResult.setTotalPage(page.getPages());
        pageResult.setRecords(page.getResult());
        return pageResult;
    }

    @Override
    public void setStatus(Byte lockStatus, List<Long> ids) {
        userMapper.setStatus(lockStatus,ids);
    }
}
