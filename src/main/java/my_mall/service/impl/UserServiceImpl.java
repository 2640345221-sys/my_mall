package my_mall.service.impl;

import cn.hutool.core.util.IdUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import my_mall.constant.MessageConstant;
import my_mall.entity.dto.LoginDTO;
import my_mall.entity.dto.UserPageDTO;
import my_mall.entity.po.User;
import my_mall.entity.vo.UserVO;
import my_mall.exception.BaseException;
import my_mall.exception.PasswordErrorException;
import my_mall.exception.RepeatKeyException;
import my_mall.exception.UserIsLockedException;
import my_mall.exception.UserNameNotExistException;
import my_mall.mapper.UserMapper;
import my_mall.result.PageResult;
import my_mall.service.UserService;
import my_mall.utils.TLUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImpl  implements UserService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private BCryptPasswordEncoder passwordEncoder;

    @SneakyThrows
    @Override
    public User login(LoginDTO loginDTO) {
        String username = loginDTO.getUsername();
        User user=userMapper.getByUsername(username);
        if(user==null){
            throw new UserNameNotExistException(MessageConstant.USERNAME_NOT_EXIST);
        }
        String password = loginDTO.getPassword();
        if(!passwordEncoder.matches(password, user.getPassword())){
            throw new PasswordErrorException(MessageConstant.ADMIN_PASSWORD_ERROR+ "，用户账号：" + loginDTO.getUsername());
        }
        if(user.getLocked()==true){
            throw new UserIsLockedException(MessageConstant.USER_LOCKED + "，用户ID：" + user.getId() + "，操作用户ID：" + TLUtils.getUserId());
        }
        return user;
    }

    @SneakyThrows
    @Override
    public void register(LoginDTO loginDTO) {
        String nickName = "user" + IdUtil.getSnowflake().nextIdStr();
        if (loginDTO.getUsername() == null || loginDTO.getUsername().trim().isEmpty()) {
            throw new BaseException(MessageConstant.USERNAME_EMPTY);
        }
        if (loginDTO.getUsername().length() > 11) {
            throw new BaseException(MessageConstant.USERNAME_TOO_LONG);
        }
        if(userMapper.getByLoginName(loginDTO.getUsername()) != null){
            throw new RepeatKeyException(MessageConstant.USERNAME_EXIST + "，用户名：" + loginDTO.getUsername() + "，操作用户ID：" + TLUtils.getUserId());
        }
        if(loginDTO.getPassword()==null){
            throw new PasswordErrorException(MessageConstant.PASSWORD_EMPTY + "，用户名：" + loginDTO.getUsername() + "，操作用户ID：" + TLUtils.getUserId());
        }
        User user=User.builder()
                .loginName(loginDTO.getUsername())
                .nickName(nickName)
                .password(passwordEncoder.encode(loginDTO.getPassword()))
                .locked(false)
                .introduceSign("")
                .build();
        userMapper.insert(user);
    }

    @Override
    public User getUserInfo() {
        Long userId= TLUtils.getUserId();
        User user=userMapper.getById(userId);
        return user;
    }

    @Override
    public void updateUserInfo(UserVO userVO) {
        Long id= TLUtils.getUserId();
        User user =new User();
        BeanUtils.copyProperties(userVO,user);
        user.setId(id);
        if(userVO.getPassword()!=null)
        user.setPassword(passwordEncoder.encode(userVO.getPassword()));
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
    public void setStatus(Integer lockStatus, List<Long> ids) {
        userMapper.setStatus(lockStatus,ids);
    }
}
