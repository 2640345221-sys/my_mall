package my_mall.service;

import my_mall.entity.dto.AdminUpdateDTO;
import my_mall.entity.dto.LoginDTO;
import my_mall.entity.po.Admin;

public interface AdminService {
    Admin login(LoginDTO loginDTO);

    void update(AdminUpdateDTO adminUpdateDTO);

    Admin getProfile();
}
