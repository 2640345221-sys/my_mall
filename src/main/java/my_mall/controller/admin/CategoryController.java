package my_mall.controller.admin;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import my_mall.entity.dto.CategoryDTO;
import my_mall.entity.po.GoodsCategory;
import my_mall.mapper.CategoryMapper;
import my_mall.result.Result;
import my_mall.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("adminCategoryController")
@RequestMapping("/api/admin/category")
@Slf4j
public class CategoryController {
    @Resource
    private CategoryService categoryService;
    @Autowired
    private CategoryMapper categoryMapper;

    @PostMapping
    public Result insert(@RequestBody CategoryDTO categoryInsertDTO) {
        categoryService.insert(categoryInsertDTO);
        return Result.success();
    }

    @DeleteMapping
    public Result delete(@RequestParam List<Long> ids ) {
        categoryService.deleteBatch(ids);
        return Result.success();
    }

    @PutMapping
    public Result update(@RequestBody CategoryDTO categoryDTO) {
        categoryService.update(categoryDTO);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<GoodsCategory> getById(@PathVariable Long id) {
        GoodsCategory category=categoryService.getById(id);
        return Result.success(category);
    }

    @GetMapping
    public Result<List<GoodsCategory>> getAll() {
        return Result.success(categoryService.getAll());
    }
}
