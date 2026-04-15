package my_mall.service;

import my_mall.entity.dto.LoginDTO;
import my_mall.entity.po.User;
import my_mall.entity.vo.UserVO;

public interface UserService {
    User login(LoginDTO loginDTO);

    void register(LoginDTO loginDTO);

    User getUserInfo();

    void updateUserInfo(UserVO userVO);
}
