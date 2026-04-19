package my_mall.controller.admin;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import my_mall.entity.dto.AdminUpdateDTO;
import my_mall.entity.dto.LoginDTO;
import my_mall.entity.po.Admin;
import my_mall.entity.properties.JwtProperties;
import my_mall.entity.vo.UserLoginVO;
import my_mall.result.Result;
import my_mall.service.AdminService;
import my_mall.utils.JwtUtils;
import my_mall.utils.TLUtils;

@Tag(name = "管理员模块")
@RestController
@RequestMapping("/api/admin")
@Slf4j
public class AdminController {
    @Resource
    private AdminService adminService;
    @Resource
    private JwtProperties jwtProperties;

    @Operation(summary = "管理员登录")
    @GetMapping("/login")
    public Result login(LoginDTO loginDTO){
        log.info("管理员登录:{}",loginDTO);
        Admin admin = adminService.login(loginDTO);
        if(admin == null){
            return Result.error("账号或密码错误");
        }
        Map<String,Object> claims = new HashMap<>();
        claims.put("userId", admin.getId());
        String token= JwtUtils.createJWTToken(jwtProperties.getAdminSecretKey(), jwtProperties.getAdminTtl(), claims);
        UserLoginVO userLoginVO = UserLoginVO.
                builder()
                .id(admin.getId())
                .nickName(admin.getNickName())
                .loginName(admin.getUsername())
                .token(token)
                .build();
        return Result.success(userLoginVO);
    }

    @Operation(summary = "更新管理员信息")
    @PutMapping("/update")
    public Result update(AdminUpdateDTO adminUpdateDTO){
        adminService.update(adminUpdateDTO);
        return Result.success();
    }

    @Operation(summary = "获取管理员信息")
    @GetMapping("/profile")
    public Result getProfile(){
        Admin admin=adminService.getProfile();
        return Result.success(admin);
    }

    @Operation(summary = "管理员登出")
    @DeleteMapping("/logout")
    public Result logout(){
        TLUtils.remove();
        return Result.success();
    }

}
