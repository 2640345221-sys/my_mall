package my_mall.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.github.benmanes.caffeine.cache.Cache;
import my_mall.service.CommonService;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.SimpleKey;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import jakarta.annotation.Resource;
import my_mall.constant.MessageConstant;
import my_mall.entity.dto.IndexConfigDTO;
import my_mall.entity.dto.IndexPageDTO;
import my_mall.entity.po.Goods;
import my_mall.entity.po.IndexConfig;
import my_mall.enums.IndexConfigTypeEnum;
import my_mall.exception.IndexConfigNotExistException;
import my_mall.mapper.GoodsMapper;
import my_mall.mapper.IndexConfigMapper;
import my_mall.result.PageResult;
import my_mall.service.IndexConfigService;
import my_mall.utils.TLUtils;
import org.springframework.transaction.annotation.Transactional;

@Service
public class IndexConfigServiceImpl implements IndexConfigService {
    @Resource
    private IndexConfigMapper indexConfigMapper;
    @Resource
    private GoodsMapper goodsMapper;
    @Resource
    private CommonService commonService;
    @Resource
    private CacheManager cacheManager;
    @Resource
    private Cache<String, Object> goodsCache;
    @Override
    public PageResult getPage(IndexPageDTO indexPageDTO) {
        PageResult pageResult = new PageResult();
        PageHelper.startPage(indexPageDTO.getPageNumber(), indexPageDTO.getPageSize());
        Page<IndexConfig> list=indexConfigMapper.getPage(indexPageDTO);
        pageResult.setTotal(list.getTotal());
        pageResult.setTotalPage(list.getPages());
        pageResult.setRecords(list.getResult());
        return pageResult;
    }

    @Override
    public IndexConfig getById(Long id) {
        IndexConfig indexConfig=indexConfigMapper.getById(id);
        if(indexConfig==null){
            throw new IndexConfigNotExistException(MessageConstant.INDEX_CONFIG_NOT_EXIST + "，配置ID：" + id + "，操作用户ID：" + TLUtils.getUserId());
        }
        return indexConfig;
    }

    @Override
    public void delete(List<Long> ids) {
        indexConfigMapper.deleteBatch(ids);
        resetIndexConfig();
    }

    @Override
    @Transactional
    public void update(IndexConfigDTO indexConfigDTO) {
        IndexConfig indexConfig=indexConfigMapper.getById(indexConfigDTO.getId());
        if(indexConfig==null){
            throw new IndexConfigNotExistException(MessageConstant.INDEX_CONFIG_NOT_EXIST + "，配置ID：" + indexConfigDTO.getId() + "，操作用户ID：" + TLUtils.getUserId());
        }
        BeanUtils.copyProperties(indexConfigDTO,indexConfig);
        indexConfigMapper.update(indexConfig);
        resetIndexConfig();
    }

    @Override
    public void insert(IndexConfigDTO indexConfigDTO) {
        IndexConfig indexConfig=new IndexConfig();
        BeanUtils.copyProperties(indexConfigDTO,indexConfig);
        indexConfigMapper.insert(indexConfig);
        resetIndexConfig();
    }

    @Override
    public List<Goods> getNewGoods() {
        String l1Key = "goods:new";
        String l2CacheName = "newCache";

        List<Goods> cached = (List<Goods>) goodsCache.getIfPresent(l1Key);
        if (cached != null) {
            return cached;
        }

        org.springframework.cache.Cache l2Cache = cacheManager.getCache(l2CacheName);
        if (l2Cache != null) {
            List<Goods> redisValue = l2Cache.get(SimpleKey.EMPTY, List.class);
            if (redisValue != null) {
                goodsCache.put(l1Key, redisValue);
                return redisValue;
            }
        }

        List<IndexConfig> list = indexConfigMapper.getByType(IndexConfigTypeEnum.NEW_GOODS.getValue());
        List<Long> ids = list.stream().map(IndexConfig::getGoodsId).collect(Collectors.toList());
        List<Goods> result = ids.isEmpty() ? new ArrayList<>() : goodsMapper.getByIdBatch(ids);

        goodsCache.put(l1Key, result);
        if (l2Cache != null) {
            l2Cache.put(SimpleKey.EMPTY, result);
        }

        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Goods> getPopularGoods() {
        String l1Key = "goods:popular";
        String l2CacheName = "popularCache";

        List<Goods> cached = (List<Goods>) goodsCache.getIfPresent(l1Key);
        if (cached != null) {
            return cached;
        }

        org.springframework.cache.Cache l2Cache = cacheManager.getCache(l2CacheName);
        if (l2Cache != null) {
            List<Goods> redisValue = l2Cache.get(SimpleKey.EMPTY, List.class);
            if (redisValue != null) {
                goodsCache.put(l1Key, redisValue);
                return redisValue;
            }
        }

        List<IndexConfig> list = indexConfigMapper.getByType(IndexConfigTypeEnum.POPULAR_GOODS.getValue());
        List<Long> ids = list.stream().map(IndexConfig::getGoodsId).collect(Collectors.toList());
        List<Goods> result = ids.isEmpty() ? new ArrayList<>() : goodsMapper.getByIdBatch(ids);

        goodsCache.put(l1Key, result);
        if (l2Cache != null) {
            l2Cache.put(SimpleKey.EMPTY, result);
        }
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Goods> getRecommendGoods() {
        String l1Key = "goods:recommend";
        String l2CacheName = "recommendCache";

        List<Goods> cached = (List<Goods>) goodsCache.getIfPresent(l1Key);
        if (cached != null) {
            return cached;
        }

        org.springframework.cache.Cache l2Cache = cacheManager.getCache(l2CacheName);
        if (l2Cache != null) {
            List<Goods> redisValue = l2Cache.get(SimpleKey.EMPTY, List.class);
            if (redisValue != null) {
                goodsCache.put(l1Key, redisValue);
                return redisValue;
            }
        }

        List<IndexConfig> list = indexConfigMapper.getByType(IndexConfigTypeEnum.RECOMMEND_GOODS.getValue());
        List<Long> ids = list.stream().map(IndexConfig::getGoodsId).collect(Collectors.toList());
        List<Goods> result = ids.isEmpty() ? new ArrayList<>() : goodsMapper.getByIdBatch(ids);

        goodsCache.put(l1Key, result);
        if (l2Cache != null) {
            l2Cache.put(SimpleKey.EMPTY, result);
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resetIndexConfig() {
        commonService.resetRecommendGoods();
        commonService.resetNewGoods();
        commonService.resetPopularGoods();

        goodsCache.invalidate("goods:new");
        goodsCache.invalidate("goods:popular");
        goodsCache.invalidate("goods:recommend");
        clearRedisCache("newCache");
        clearRedisCache("popularCache");
        clearRedisCache("recommendCache");

        putToRedis("newCache", getNewGoods());
        putToRedis("popularCache", getPopularGoods());
        putToRedis("recommendCache", getRecommendGoods());
    }

    private void clearRedisCache(String cacheName) {
        org.springframework.cache.Cache cache = cacheManager.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        }
    }

    private void putToRedis(String cacheName, Object value) {
        org.springframework.cache.Cache cache = cacheManager.getCache(cacheName);
        if (cache != null) {
            cache.put(SimpleKey.EMPTY, value);
        }
    }
}
