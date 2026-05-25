package my_mall.service.impl;

import jakarta.annotation.Resource;
import my_mall.constant.MessageConstant;
import my_mall.entity.dto.CategoryDTO;
import my_mall.entity.po.GoodsCategory;
import my_mall.entity.vo.IndexCategoryVO;
import my_mall.exception.CategoryNotExistException;
import my_mall.mapper.CategoryMapper;
import my_mall.service.CategoryService;
import my_mall.utils.TLUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        List<IndexCategoryVO> result = new ArrayList<>();
        for (GoodsCategory c : gList) {
            IndexCategoryVO vo = new IndexCategoryVO();
            vo.setId(c.getId());
            vo.setLevel(c.getLevel());
            vo.setName(c.getName());
            vo.setParentId(c.getParentId());
            vo.setChildren(new ArrayList<>());
            voMap.put(c.getId(), vo);
        }

        for (IndexCategoryVO vo : voMap.values()) {
            Long pid = vo.getParentId();
            if (pid == 0) {
                result.add(vo);
            } else {
                IndexCategoryVO parent = voMap.get(pid);
                if (parent != null) {
                    parent.getChildren().add(vo);
                }
            }
        }
        return result;
    }

    @Override
    public void insert(CategoryDTO categoryInsertDTO) {
        GoodsCategory category = new GoodsCategory();
        BeanUtils.copyProperties(categoryInsertDTO, category);
        categoryMapper.insert(category);
    }

    @Override
    public void deleteBatch(List<Long> ids) {
        categoryMapper.deleteBatch(ids);
    }

    @Override
    public void update(CategoryDTO categoryDTO) {
        GoodsCategory category =categoryMapper.getById(categoryDTO.getId());
        if(category==null){
            throw new CategoryNotExistException(MessageConstant.CATEGORY_NOT_EXIST + "，分类ID：" + categoryDTO.getId() + "，操作用户ID：" + TLUtils.getUserId());
        }
        BeanUtils.copyProperties(categoryDTO, category);
        categoryMapper.update(category);
    }

    @Override
    public GoodsCategory getById(Long id) {
        GoodsCategory category = categoryMapper.getById(id);
        if(category==null){
            throw new CategoryNotExistException(MessageConstant.CATEGORY_NOT_EXIST + "，分类ID：" + id + "，操作用户ID：" + TLUtils.getUserId());
        }
        return category;
    }

    @Override
    public List<GoodsCategory> getAll() {
        return categoryMapper.getAll();
    }
}
