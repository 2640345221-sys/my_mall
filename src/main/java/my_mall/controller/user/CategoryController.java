package my_mall.controller.user;

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

@RestController("userCategoryController")
@RequestMapping("/api/user/category")
@Slf4j
public class CategoryController {
    @Resource
    private CategoryService categoryService;
    @GetMapping
    public Result getCategory() {
        List<IndexCategoryVO> indexCategoryVOs = categoryService.getCategory();
        return Result.success(indexCategoryVOs);
    }
}
