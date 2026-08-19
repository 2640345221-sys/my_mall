package my_mall.service.impl;

import com.github.benmanes.caffeine.cache.Cache;
import jakarta.annotation.Resource;
import my_mall.config.CacheEvictListener;
import my_mall.constant.MessageConstant;
import my_mall.entity.dto.CategoryDTO;
import my_mall.entity.po.GoodsCategory;
import my_mall.entity.vo.IndexCategoryVO;
import my_mall.exception.CategoryNotExistException;
import my_mall.mapper.CategoryMapper;
import my_mall.service.CategoryService;
import my_mall.utils.TLUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CategoryServiceImpl implements CategoryService {

    private static final String CACHE_NAME = "categoryCache";

    @Resource
    private CategoryMapper categoryMapper;
    @Resource
    private Cache<String, Object> categoryCache;
    @Resource
    private CacheManager cacheManager;
    @Resource(name = "stringRedisTemplate")
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public List<IndexCategoryVO> getCategory() {
        String key = "tree";

        //先查本地缓存 Caffeine（L1）
        List<IndexCategoryVO> cached = (List<IndexCategoryVO>) categoryCache.getIfPresent(key);
        if (cached != null) {
            return cached;
        }

        //本地没有，查 Redis 二级缓存（L2）  因为引入了caffeine的cache，所以下面的cache添加前缀
        org.springframework.cache.Cache redisCache = cacheManager.getCache(CACHE_NAME);
        if (redisCache != null) {
            List<IndexCategoryVO> redisValue = redisCache.get(key, List.class);
            if (redisValue != null) {
                //把一级缓存写进去
                categoryCache.put(key, redisValue);
                return redisValue;
            }
        }

        //两级都没有，查数据库构建分类树，并写回两级缓存
        List<IndexCategoryVO> result = buildTree(categoryMapper.getAll());

        categoryCache.put(key, result);
        if (redisCache != null) {
            redisCache.put(key, result);
        }
        return result;
    }

    @Override
    public void insert(CategoryDTO categoryInsertDTO) {
        GoodsCategory category = new GoodsCategory();
        BeanUtils.copyProperties(categoryInsertDTO, category);
        categoryMapper.insert(category);
        evictCategoryCaches();
    }

    @Override
    public void deleteBatch(List<Long> ids) {
        categoryMapper.deleteBatch(ids);
        evictCategoryCaches();
    }

    @Override
    public void update(CategoryDTO categoryDTO) {
        GoodsCategory category = categoryMapper.getById(categoryDTO.getId());
        if (category == null) {
            throw new CategoryNotExistException(MessageConstant.CATEGORY_NOT_EXIST + "，分类ID：" + categoryDTO.getId() + "，操作用户ID：" + TLUtils.getUserId());
        }
        BeanUtils.copyProperties(categoryDTO, category);
        categoryMapper.update(category);
        evictCategoryCaches();
    }

    @Override
    public GoodsCategory getById(Long id) {
        GoodsCategory category = categoryMapper.getById(id);
        if (category == null) {
            throw new CategoryNotExistException(MessageConstant.CATEGORY_NOT_EXIST + "，分类ID：" + id + "，操作用户ID：" + TLUtils.getUserId());
        }
        return category;
    }

    @Override
    public List<GoodsCategory> getAll() {
        return categoryMapper.getAll();
    }

    private void evictCategoryCaches() {
        categoryCache.invalidateAll();
        org.springframework.cache.Cache redisCache = cacheManager.getCache(CACHE_NAME);
        if (redisCache != null) {
            redisCache.clear();
        }
        //发布消息，通知其他实例清它们的本地缓存
        stringRedisTemplate.convertAndSend(CacheEvictListener.CACHE_EVICT_TOPIC, CacheEvictListener.CATEGORY_CACHE);
    }

    //把扁平分类列表组装成树形结构：先全部转VO，再按 parentId 挂到父节点下
    private List<IndexCategoryVO> buildTree(List<GoodsCategory> gList) {
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
}
