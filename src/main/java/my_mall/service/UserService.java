package my_mall.service;

import my_mall.entity.dto.LoginDTO;
import my_mall.entity.dto.UserPageDTO;
import my_mall.entity.po.User;
import my_mall.entity.vo.UserVO;
import my_mall.result.PageResult;

import java.util.List;

public interface UserService {
    User login(LoginDTO loginDTO);

    void register(LoginDTO loginDTO);

    User getUserInfo();

    void updateUserInfo(UserVO userVO);

    PageResult page(UserPageDTO userPageDTO);

    void setStatus(Integer lockStatus, List<Long> ids);
}
