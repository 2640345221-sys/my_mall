package my_mall.controller.admin;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import my_mall.entity.dto.AdminUpdateDTO;
import my_mall.entity.dto.LoginDTO;
import my_mall.entity.po.Admin;
import my_mall.entity.po.User;
import my_mall.entity.properties.JwtProperties;
import my_mall.entity.vo.UserLoginVO;
import my_mall.mapper.AdminMapper;
import my_mall.result.Result;
import my_mall.service.AdminService;
import my_mall.utils.JwtUtils;
import my_mall.utils.TLUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@Slf4j
public class AdminController {
    @Resource
    private AdminService adminService;
    @Resource
    private JwtProperties jwtProperties;

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

    @PutMapping("/update")
    public Result update(AdminUpdateDTO adminUpdateDTO){
        adminService.update(adminUpdateDTO);
        return Result.success();
    }

    @GetMapping("/profile")
    public Result<Admin> getProfile(){
        Admin admin=adminService.getProfile();
        return Result.success(admin);
    }

    @DeleteMapping("/logout")
    public Result logout(){
        TLUtils.remove();
        return Result.success();
    }


}
