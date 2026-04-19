package my_mall.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import my_mall.entity.dto.UserPageDTO;
import my_mall.result.PageResult;
import my_mall.result.Result;
import my_mall.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "用户管理模块")
@RestController("adminUserController")
@RequestMapping("/api/admin/user")
@Slf4j
public class UserController {
    @Resource
    private UserService userService;

    @Operation(summary = "分页查询用户")
    @GetMapping
    public Result<PageResult> page(UserPageDTO userPageDTO) {
        PageResult pageResult = userService.page(userPageDTO);
        return Result.success(pageResult);
    }

    @Operation(summary = "修改用户状态")
    @PutMapping("/{lockStatus}")
    public Result setStatus(@PathVariable("lockStatus") Byte lockStatus,@RequestBody List<Long> ids) {
        userService.setStatus(lockStatus,ids);
        return Result.success();
    }
}
