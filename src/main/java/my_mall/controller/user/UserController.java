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
import my_mall.constant.MessageConstant;
import my_mall.annotation.OperationLog;
import my_mall.entity.dto.LoginDTO;
import my_mall.entity.po.User;
import my_mall.entity.properties.JwtProperties;
import my_mall.entity.vo.UserLoginVO;
import my_mall.entity.vo.UserVO;
import my_mall.result.Result;
import my_mall.service.UserService;
import my_mall.utils.JwtUtils;

@Tag(name = "用户模块")
@RestController("userUserController")
@RequestMapping("/api/user")
@Slf4j
//用户端：登录、注册、个人信息
public class UserController {
    @Resource
    private JwtProperties jwtProperties;
    @Resource
    private UserService userService;

    @Operation(summary = "用户登录")
    @OperationLog(module = "用户模块", type = "登录", description = "用户登录",
            recordParams = true, recordResult = true)
    @PostMapping("/login")
    public Result login(@RequestBody LoginDTO loginDTO){
        User user = userService.login(loginDTO);
        if(user == null){
            return Result.error(MessageConstant.LOGIN_ERROR);
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

    @Operation(summary = "用户登出")
    @OperationLog(module = "用户模块", type = "登出", description = "用户登出",
            recordParams = true, recordResult = true)
    @PostMapping("/logout")
    public Result logout(){
        return Result.success();
    }

    @Operation(summary = "用户注册")
    @OperationLog(module = "用户模块", type = "注册", description = "用户注册",
            recordParams = true, recordResult = true)
    @PostMapping("/register")
    public Result userRegister(@RequestBody LoginDTO loginDTO){
        userService.register(loginDTO);
        return Result.success();
    }

    @Operation(summary = "获取用户信息")
    @OperationLog(module = "用户模块", type = "查询", description = "获取用户信息",
            recordParams = true, recordResult = true)
    @GetMapping("/info")
    public Result getUserInfo(){
        User user=userService.getUserInfo();
        return Result.success(user);
    }

    @Operation(summary = "更新用户信息")
    @OperationLog(module = "用户模块", type = "更新", description = "更新用户信息",
            recordParams = true, recordResult = true)
    @PutMapping("/info")
    public Result updateUserInfo(@RequestBody UserVO userVO){
        userService.updateUserInfo(userVO);
        return Result.success();
    }

}
