package my_mall.service.impl;

import jakarta.annotation.Resource;
import my_mall.entity.dto.LoginDTO;
import my_mall.entity.po.User;
import my_mall.entity.vo.UserVO;
import my_mall.mapper.UserMapper;
import my_mall.service.UserService;
import my_mall.utils.TLUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl  implements UserService {
    @Resource
    private UserMapper userMapper;
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
        return user;
    }

    @Override
    public void register(LoginDTO loginDTO) {
        //随机生成一个用户名，避免与数据库已有用户冲突
        String nickName = "user" + System.currentTimeMillis();
        while (userMapper.getByNickName(nickName)!=null) {
            nickName = "user" + System.currentTimeMillis() + (int)(Math.random() * 100);
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
        user.setPassword(DigestUtils.md5DigestAsHex(userVO.getPassword().getBytes()));
        userMapper.update(user);
    }
}
