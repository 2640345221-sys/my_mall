package my_mall.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "分类管理模块")
@RestController("adminCategoryController")
@RequestMapping("/api/admin/category")
@Slf4j
public class CategoryController {
    @Resource
    private CategoryService categoryService;

    @Operation(summary = "新增分类")
    @PostMapping
    public Result insert(@RequestBody CategoryDTO categoryInsertDTO) {
        log.info("新增商品分类{}",categoryInsertDTO);
        categoryService.insert(categoryInsertDTO);
        return Result.success();
    }

    @Operation(summary = "批量删除分类")
    @DeleteMapping
    public Result delete(@RequestParam List<Long> ids ) {
        log.info("删除商品分类,ids:{}",ids);
        categoryService.deleteBatch(ids);
        return Result.success();
    }

    @Operation(summary = "更新分类")
    @PutMapping
    public Result update(@RequestBody CategoryDTO categoryDTO) {
        log.info("更新商品分类{}",categoryDTO);
        categoryService.update(categoryDTO);
        return Result.success();
    }

    @Operation(summary = "根据ID查询分类")
    @GetMapping("/{id}")
    public Result<GoodsCategory> getById(@PathVariable Long id) {
        log.info("查询id为{}的商品分类",id);
        GoodsCategory category=categoryService.getById(id);
        return Result.success(category);
    }

    @Operation(summary = "获取所有分类")
    @GetMapping
    public Result<List<GoodsCategory>> getAll() {
        log.info("获取所有商品分类");
        return Result.success(categoryService.getAll());
    }
}
