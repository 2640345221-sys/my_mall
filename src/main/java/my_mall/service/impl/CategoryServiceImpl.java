package my_mall.service.impl;

import jakarta.annotation.Resource;
import my_mall.entity.po.GoodsCategory;
import my_mall.entity.vo.IndexCategoryVO;
import my_mall.mapper.CategoryMapper;
import my_mall.service.CategoryService;
import org.springframework.stereotype.Service;

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
}
