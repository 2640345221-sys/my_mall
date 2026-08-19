package my_mall.controller.user;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import my_mall.annotation.OperationLog;
import my_mall.entity.vo.IndexCategoryVO;
import my_mall.result.Result;
import my_mall.service.CategoryService;

@Tag(name = "用户分类模块")
@RestController("userCategoryController")
@RequestMapping("/api/user/category")
@Slf4j
//用户端：获取商品分类树
public class CategoryController {
    @Resource
    private CategoryService categoryService;

    @Operation(summary = "获取分类列表")
    @OperationLog(module = "用户分类模块", type = "查询", description = "获取分类列表",
            recordParams = true, recordResult = true)
    @GetMapping
    public Result<List<IndexCategoryVO>> getCategory() {
        List<IndexCategoryVO> indexCategoryVOs = categoryService.getCategory();
        return Result.success(indexCategoryVOs);
    }
}
