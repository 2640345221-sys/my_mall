package my_mall.controller.admin;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import my_mall.annotation.OperationLog;
import my_mall.entity.dto.UserPageDTO;
import my_mall.result.PageResult;
import my_mall.result.Result;
import my_mall.service.UserService;
import my_mall.utils.TLUtils;

@Tag(name = "用户管理模块")
@RestController("adminUserController")
@RequestMapping("/api/admin/user")
@Slf4j
//管理端：用户分页查询和锁定
public class UserController {
    @Resource
    private UserService userService;

    @Operation(summary = "分页查询用户")
    @OperationLog(module = "管理端用户模块", type = "查询", description = "分页查询用户",
            recordParams = true, recordResult = true)
    @GetMapping
    public Result<PageResult> page(UserPageDTO userPageDTO) {
        PageResult pageResult = userService.page(userPageDTO);
        return Result.success(pageResult);
    }

    @Operation(summary = "修改用户状态")
    @OperationLog(module = "管理端用户模块", type = "更新", description = "修改用户状态",
            recordParams = true, recordResult = true)
    @PutMapping("/{lockStatus}")
    public Result setStatus(@PathVariable("lockStatus") Integer lockStatus,@RequestBody List<Long> ids) {
        userService.setStatus(lockStatus,ids);
        return Result.success();
    }
}
