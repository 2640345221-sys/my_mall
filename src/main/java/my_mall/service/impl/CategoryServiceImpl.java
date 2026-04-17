package my_mall.service.impl;

import jakarta.annotation.Resource;
import my_mall.entity.dto.CategoryDTO;
import my_mall.entity.po.GoodsCategory;
import my_mall.entity.vo.IndexCategoryVO;
import my_mall.mapper.CategoryMapper;
import my_mall.service.CategoryService;
import my_mall.utils.TLUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Resource
    private CategoryMapper categoryMapper;
    @Override
    public List<IndexCategoryVO> getCategory() {
        List<GoodsCategory> gList = categoryMapper.getAll();

        Map<Long, IndexCategoryVO> voMap = new HashMap<>();
        for (GoodsCategory c : gList) {
            IndexCategoryVO vo = new IndexCategoryVO();
            vo.setId(c.getId());
            vo.setName(c.getName());
            vo.setChildren(new ArrayList<>());
            voMap.put(c.getId(), vo);
        }

        List<IndexCategoryVO> result = new ArrayList<>();
        for (GoodsCategory c : gList) {
            if (c.getParentId() == 0) {
                // 一级分类
                IndexCategoryVO first = voMap.get(c.getId());
                result.add(first);

                // 找二级分类
                for (GoodsCategory c2 : gList) {
                    if (c2.getParentId() != 0 && c2.getParentId().equals(c.getId())) {
                        IndexCategoryVO second = voMap.get(c2.getId());
                        first.getChildren().add(second);

                        // 找三级分类
                        for (GoodsCategory c3 : gList) {
                            if (c3.getParentId() != 0 && c3.getParentId().equals(c2.getId())) {
                                IndexCategoryVO third = voMap.get(c3.getId());
                                second.getChildren().add(third);
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    @Override
    public void insert(CategoryDTO categoryInsertDTO) {
        GoodsCategory category = new GoodsCategory();
        BeanUtils.copyProperties(categoryInsertDTO, category);
        category.setCreateTime(LocalDateTime.now());
        category.setUpdateTime(LocalDateTime.now());
        category.setUpdateUser(Math.toIntExact(TLUtils.getUserId()));
        category.setCreateUser(Math.toIntExact(TLUtils.getUserId()));
        categoryMapper.insert(category);
    }

    @Override
    public void deleteBatch(List<Long> ids) {
        categoryMapper.deleteBatch(ids);
    }

    @Override
    public void update(CategoryDTO categoryDTO) {
        GoodsCategory category = new GoodsCategory();
        BeanUtils.copyProperties(categoryDTO, category);
        category.setUpdateTime(LocalDateTime.now());
        category.setUpdateUser(Math.toIntExact(TLUtils.getUserId()));
        categoryMapper.update(category);
    }

    @Override
    public GoodsCategory getById(Long id) {
        return categoryMapper.getById(id);
    }

    @Override
    public List<GoodsCategory> getAll() {
        return categoryMapper.getAll();
    }
}
