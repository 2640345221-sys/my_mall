package my_mall.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import my_mall.annotation.OperationLog;
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
    @OperationLog(module = "管理端商品分类模块",type = "插入",description = "新增商品分类",
            recordParams = true,recordResult = true)
    public Result insert(@RequestBody CategoryDTO categoryInsertDTO) {
        categoryService.insert(categoryInsertDTO);
        return Result.success();
    }

    @Operation(summary = "批量删除分类")
    @DeleteMapping
    @OperationLog(module = "管理端商品分类模块",type = "删除",description = "删除指定商品分类",
            recordParams = true,recordResult = true)
    public Result delete(@RequestParam List<Long> ids ) {
        categoryService.deleteBatch(ids);
        return Result.success();
    }

    @Operation(summary = "更新分类")
    @PutMapping
    @OperationLog(module = "管理端商品分类模块",type = "更新",description = "更新商品分类",
            recordParams = true,recordResult = true)
    public Result update(@RequestBody CategoryDTO categoryDTO) {
        categoryService.update(categoryDTO);
        return Result.success();
    }

    @Operation(summary = "根据ID查询分类")
    @GetMapping("/{id}")
    @OperationLog(module = "管理端商品分类模块",type = "查询",description = "查询指定ID商品分类",
            recordParams = true,recordResult = true)
    public Result<GoodsCategory> getById(@PathVariable Long id) {
        GoodsCategory category=categoryService.getById(id);
        return Result.success(category);
    }

    @Operation(summary = "获取所有分类")
    @GetMapping
    @OperationLog(module = "管理端商品分类模块",type = "查询",description = "查询所有商品分类",
            recordParams = true,recordResult = true)
    public Result<List<GoodsCategory>> getAll() {
        return Result.success(categoryService.getAll());
    }
}
