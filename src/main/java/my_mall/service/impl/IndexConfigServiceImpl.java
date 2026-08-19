package my_mall.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.SimpleKey;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import my_mall.config.CacheEvictListener;
import my_mall.constant.MessageConstant;
import my_mall.entity.dto.IndexConfigDTO;
import my_mall.entity.dto.IndexPageDTO;
import my_mall.entity.po.Goods;
import my_mall.entity.po.IndexConfig;
import my_mall.enums.IndexConfigTypeEnum;
import my_mall.exception.IndexConfigNotExistException;
import my_mall.mapper.GoodsMapper;
import my_mall.mapper.IndexConfigMapper;
import my_mall.mapper.ShoppingCartMapper;
import my_mall.result.PageResult;
import my_mall.service.IndexConfigService;
import my_mall.utils.TLUtils;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class IndexConfigServiceImpl implements IndexConfigService {
    @Resource
    private IndexConfigMapper indexConfigMapper;
    @Resource
    private GoodsMapper goodsMapper;
    @Resource
    private ShoppingCartMapper shoppingCartMapper;
    @Resource
    private CacheManager cacheManager;
    @Resource
    private Cache<String, Object> goodsCache;
    @Resource(name = "stringRedisTemplate")
    private StringRedisTemplate stringRedisTemplate;
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
    //获取新品商品（两级缓存：Caffeine → Redis → 数据库）
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
    //获取热销商品（两级缓存，同 getNewGoods）
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
    //获取推荐商品（两级缓存，同 getNewGoods）
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
    //重置首页配置：重新计算三个栏目，然后清空并预热两级缓存
    public void resetIndexConfig() {
        resetRecommendGoods();
        resetNewGoods();
        resetPopularGoods();

        goodsCache.invalidate("goods:new");
        goodsCache.invalidate("goods:popular");
        goodsCache.invalidate("goods:recommend");
        clearRedisCache("newCache");
        clearRedisCache("popularCache");
        clearRedisCache("recommendCache");

        putToRedis("newCache", getNewGoods());
        putToRedis("popularCache", getPopularGoods());
        putToRedis("recommendCache", getRecommendGoods());
        //发布消息，通知其他实例清它们的本地缓存
        stringRedisTemplate.convertAndSend(CacheEvictListener.CACHE_EVICT_TOPIC, CacheEvictListener.INDEX_CONFIG_CACHE);
    }

    @Override
    //重新设置最新商品（首页新品栏）
    public void resetNewGoods() {
        try {
            log.info("开始重新设置最新商品");
            indexConfigMapper.deleteByType(IndexConfigTypeEnum.NEW_GOODS.getValue());
            List<Goods> latestGoods = goodsMapper.getLatestGoods(10);
            if (latestGoods.isEmpty()) {
                log.info("没有找到可用的最新商品");
                return;
            }
            List<IndexConfig> newConfigs = latestGoods.stream()
                    .map(goods -> IndexConfig.builder()
                            .name(goods.getName())
                            .type(IndexConfigTypeEnum.NEW_GOODS.getValue())
                            .goodsId(goods.getId())
                            .redirectUrl("/goods/detail/" + goods.getId())
                            .rank(0)
                            .createTime(LocalDateTime.now())
                            .updateTime(LocalDateTime.now())
                            .createUser(0L)
                            .updateUser(0L)
                            .build())
                    .collect(Collectors.toList());
            if(newConfigs!=null&&!newConfigs.isEmpty()){
                indexConfigMapper.insertBatch(newConfigs);
            }
            log.info("成功设置 {} 个最新商品", latestGoods.size());
        } catch (Exception e) {
            log.error("重新设置最新商品失败", e);
            throw new RuntimeException(MessageConstant.RESET_NEW_GOODS_ERROR + ": " + e.getMessage());
        }
    }

    @Override
    //重新设置热销商品（首页热销栏）
    public void resetPopularGoods() {
        try {
            log.info("开始重新设置热销商品");
            indexConfigMapper.deleteByType(IndexConfigTypeEnum.POPULAR_GOODS.getValue());
            List<Long> hotGoodsIds = shoppingCartMapper.selectTopSellingGoodsIds(10);
            if (hotGoodsIds.isEmpty()) {
                hotGoodsIds = goodsMapper.getLatestGoods(10).stream().map(Goods::getId).collect(Collectors.toList());
            }
            if (hotGoodsIds.isEmpty()) {
                log.info("没有找到热销商品");
                return;
            }
            List<Goods> hotGoods = goodsMapper.getByIdBatch(hotGoodsIds);
            List<IndexConfig> configs = hotGoods.stream()
                    .map(goods -> IndexConfig.builder()
                            .name(goods.getName())
                            .type(IndexConfigTypeEnum.POPULAR_GOODS.getValue())
                            .goodsId(goods.getId())
                            .redirectUrl("/goods/detail/" + goods.getId())
                            .rank(0)
                            .createTime(LocalDateTime.now())
                            .updateTime(LocalDateTime.now())
                            .createUser(0L)
                            .updateUser(0L)
                            .build())
                    .collect(Collectors.toList());
            if(configs!=null&&!configs.isEmpty()){
                indexConfigMapper.insertBatch(configs);
            }
            log.info("成功设置 {} 个热销商品", hotGoods.size());
        } catch (Exception e) {
            log.error("重新设置热销商品失败", e);
            throw new RuntimeException(MessageConstant.RESET_POPULAR_GOODS_ERROR + ": " + e.getMessage());
        }
    }

    @Override
    //重新设置推荐商品（首页推荐栏）
    public void resetRecommendGoods() {
        try {
            log.info("开始重新设置推荐商品");
            indexConfigMapper.deleteByType(IndexConfigTypeEnum.RECOMMEND_GOODS.getValue());
            List<Long> hotGoodsIds = shoppingCartMapper.selectTopSellingGoodsIds(10);
            if (hotGoodsIds.isEmpty()) {
                hotGoodsIds = goodsMapper.getLatestGoods(10).stream().map(Goods::getId).collect(Collectors.toList());
            }
            if (hotGoodsIds.isEmpty()) {
                log.info("没有找到推荐商品");
                return;
            }
            List<Goods> hotGoods = goodsMapper.getByIdBatch(hotGoodsIds);
            List<IndexConfig> configs = hotGoods.stream()
                    .map(goods -> IndexConfig.builder()
                            .name(goods.getName())
                            .type(IndexConfigTypeEnum.RECOMMEND_GOODS.getValue())
                            .goodsId(goods.getId())
                            .redirectUrl("/goods/detail/" + goods.getId())
                            .rank(0)
                            .createTime(LocalDateTime.now())
                            .updateTime(LocalDateTime.now())
                            .createUser(0L)
                            .updateUser(0L)
                            .build())
                    .collect(Collectors.toList());
            if(configs!=null&&!configs.isEmpty()){
                indexConfigMapper.insertBatch(configs);
            }
            log.info("成功设置 {} 个推荐商品", hotGoods.size());
        } catch (Exception e) {
            log.error("重新设置推荐商品失败", e);
            throw new RuntimeException(MessageConstant.RESET_RECOMMEND_GOODS_ERROR + ": " + e.getMessage());
        }
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
