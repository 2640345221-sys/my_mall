package my_mall.service.impl;

import jakarta.annotation.Resource;
import my_mall.entity.dto.AdminUpdateDTO;
import my_mall.entity.dto.LoginDTO;
import my_mall.entity.po.Admin;
import my_mall.mapper.AdminMapper;
import my_mall.service.AdminService;
import my_mall.utils.TLUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
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
            return null;
        }
        String password = loginDTO.getPassword();
        if(!DigestUtils.md5DigestAsHex(password.getBytes()).equals(admin.getPassword())){
            return null;
        }
        return admin;
    }

    @Override
    public void update(AdminUpdateDTO adminUpdateDTO) {
        Admin admin=new Admin();
        BeanUtils.copyProperties(adminUpdateDTO,admin);
        admin.setId(TLUtils.getUserId());
        adminMapper.update(admin);
    }

    @Override
    public Admin getProfile() {
        Long id=TLUtils.getUserId();
        Admin admin=adminMapper.getById(id);
        return admin;
    }
}
