package my_mall.controller.user;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "用户模块")
@RestController("userUserController")
@RequestMapping("/api/user")
@Slf4j
public class UserController {
    @Resource
    private JwtProperties jwtProperties;
    @Resource
    private UserService userService;

    @Operation(summary = "用户登录")
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
        log.info("userLoginVO:{}",userLoginVO);
        return Result.success(userLoginVO);
    }

    @Operation(summary = "用户登出")
    @PostMapping("/logout")
    public Result logout(){
        log.info("用户退出:{}",TLUtils.getUserId());
        TLUtils.remove();
        return Result.success();
    }

    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public Result userRegister(@RequestBody LoginDTO loginDTO){
        log.info("有用户开始注册:{}",loginDTO);
        userService.register(loginDTO);
        return Result.success();
    }

    @Operation(summary = "获取用户信息")
    @GetMapping("/info")
    public Result getUserInfo(){
        log.info("开始获取用户信息");
        User user=userService.getUserInfo();
        return Result.success(user);
    }

    @Operation(summary = "更新用户信息")
    @PutMapping("/info")
    public Result updateUserInfo(@RequestBody UserVO userVO){
        log.info("开始更新用户信息{}",userVO);
        userService.updateUserInfo(userVO);
        return Result.success();
    }

}
