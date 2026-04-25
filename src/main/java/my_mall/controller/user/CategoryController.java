package my_mall.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import my_mall.entity.vo.IndexCategoryVO;
import my_mall.result.Result;
import my_mall.service.CategoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "用户分类模块")
@RestController("userCategoryController")
@RequestMapping("/api/user/category")
@Slf4j
public class CategoryController {
    @Resource
    private CategoryService categoryService;

    @Operation(summary = "获取分类列表")
    @GetMapping
    public Result<List<IndexCategoryVO>> getCategory() {
        log.info("开始获取分类列表");
        List<IndexCategoryVO> indexCategoryVOs = categoryService.getCategory();
        return Result.success(indexCategoryVOs);
    }
}
