package my_mall.service.impl;

import jakarta.annotation.Resource;
import my_mall.constant.MessageConstant;
import my_mall.entity.dto.AdminUpdateDTO;
import my_mall.entity.dto.LoginDTO;
import my_mall.entity.po.Admin;
import my_mall.exception.PasswordErrorException;
import my_mall.exception.UserNameNotExistException;
import my_mall.mapper.AdminMapper;
import my_mall.service.AdminService;
import my_mall.utils.TLUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
@Service
public class AdminServiceImpl implements AdminService {
    @Resource
    private AdminMapper adminMapper;
    @Override
    public Admin login(LoginDTO loginDTO) {
        String username = loginDTO.getUsername();
        Admin admin=adminMapper.getByUsername(username);
        if(admin==null){
            throw new UserNameNotExistException(MessageConstant.ADMIN_NOT_EXIST + "，用户名：" + username + "，操作用户ID：" + TLUtils.getUserId());
        }
        String password = loginDTO.getPassword();
        if(!DigestUtils.md5DigestAsHex(password.getBytes()).equals(admin.getPassword())){
            throw new PasswordErrorException(MessageConstant.ADMIN_PASSWORD_ERROR + "，用户名：" + username + "，操作用户ID：" + TLUtils.getUserId());
        }
        return admin;
    }

    @Override
    @Transactional
    public void update(AdminUpdateDTO adminUpdateDTO) {
        Admin admin=adminMapper.getByUsername(adminUpdateDTO.getUsername());
        if(admin==null){
            throw new UserNameNotExistException(MessageConstant.ADMIN_NOT_EXIST + "，用户名：" + adminUpdateDTO.getUsername() + "，操作用户ID：" + TLUtils.getUserId());
        }
        BeanUtils.copyProperties(adminUpdateDTO,admin);
        admin.setId(TLUtils.getUserId());
        adminMapper.update(admin);
    }

    @Override
    public Admin getProfile() {
        Long id=TLUtils.getUserId();
        Admin admin=adminMapper.getById(id);
        if(admin==null){
            throw new UserNameNotExistException(MessageConstant.ADMIN_NOT_EXIST + "，用户ID：" + id + "，操作用户ID：" + TLUtils.getUserId());
        }
        return admin;
    }
}
