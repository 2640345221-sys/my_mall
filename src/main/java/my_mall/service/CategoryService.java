package my_mall.service;

import my_mall.entity.dto.CategoryDTO;
import my_mall.entity.po.GoodsCategory;
import my_mall.entity.vo.IndexCategoryVO;

import java.util.List;

public interface CategoryService {
    List<IndexCategoryVO> getCategory();

    void insert(CategoryDTO categoryInsertDTO);

    void deleteBatch(List<Long> ids);

    void update(CategoryDTO categoryDTO);

    GoodsCategory getById(Long id);

    List<GoodsCategory> getAll();
}
