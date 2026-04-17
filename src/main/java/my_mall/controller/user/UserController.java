package my_mall.controller.user;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import my_mall.entity.dto.LoginDTO;
import my_mall.entity.po.User;

import my_mall.entity.properties.JwtProperties;
import my_mall.entity.vo.UserLoginVO;
import my_mall.entity.vo.UserVO;
import my_mall.result.Result;
import my_mall.service.UserService;
import my_mall.utils.JwtUtils;
import my_mall.utils.TLUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@Slf4j
public class UserController {
    @Resource
    private JwtProperties jwtProperties;
    @Resource
    private UserService userService;
    @PostMapping("/login")
    public Result login(@RequestBody LoginDTO loginDTO){
        log.info("用户登录:{}",loginDTO);
        User user = userService.login(loginDTO);
        if(user == null){
            return Result.error("账号或密码错误");
        }
        Map<String,Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        String token= JwtUtils.createJWTToken(jwtProperties.getUserSecretKey(), jwtProperties.getUserTtl(), claims);
        UserLoginVO userLoginVO = UserLoginVO.
                builder()
                .id(user.getId())
                .nickName(user.getNickName())
                .loginName(user.getLoginName())
                .token(token)
                .build();
        return Result.success(userLoginVO);
    }

    @PostMapping("/logout")
    public Result logout(){
        TLUtils.remove();
        return Result.success();
    }

    @PostMapping("/register")
    public Result userRegister(@RequestBody LoginDTO loginDTO){
        log.info("有用户开始注册:{}",loginDTO);
        userService.register(loginDTO);
        return Result.success();
    }

    @GetMapping("/info")
    public Result<User> getUserInfo(){
        log.info("开始获取用户信息");
        User user=userService.getUserInfo();
        return Result.success(user);
    }

    @PutMapping("/info")
    public Result updateUserInfo(@RequestBody UserVO userVO){
        userService.updateUserInfo(userVO);
        return Result.success();
    }

}
