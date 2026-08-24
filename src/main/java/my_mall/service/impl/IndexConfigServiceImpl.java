package my_mall.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
import my_mall.entity.dto.GoodsCacheValue;
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
    //逻辑过期窗口：物理 TTL 由 Caffeine/RedisCacheConfig 控制，逻辑过期提前触发异步刷新，防热点击穿
    private static final long LOGICAL_TTL_MS = 30 * 60 * 1000L;
    //正在异步刷新的缓存 key 集合，用于去重：同一 key 同一时刻只允许一个刷新任务
    private final Set<String> refreshKeys = ConcurrentHashMap.newKeySet();
    private final ExecutorService asyncExecutor = Executors.newFixedThreadPool(2);
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
    //获取新品商品（两级缓存 + 逻辑过期：过期返回旧数据 + 异步刷新，防热点击穿）
    public List<Goods> getNewGoods() {
        return getGoodsCached("goods:new", "newCache", IndexConfigTypeEnum.NEW_GOODS);
    }

    @Override
    //获取热销商品（两级缓存 + 逻辑过期，同 getNewGoods）
    public List<Goods> getPopularGoods() {
        return getGoodsCached("goods:popular", "popularCache", IndexConfigTypeEnum.POPULAR_GOODS);
    }

    @Override
    //获取推荐商品（两级缓存 + 逻辑过期，同 getNewGoods）
    public List<Goods> getRecommendGoods() {
        return getGoodsCached("goods:recommend", "recommendCache", IndexConfigTypeEnum.RECOMMEND_GOODS);
    }

    //统一缓存读取路径：L1 Caffeine → L2 Redis → DB，命中过期数据时先返回旧数据再异步刷新
    @SuppressWarnings("unchecked")
    private List<Goods> getGoodsCached(String l1Key, String l2CacheName, IndexConfigTypeEnum type) {
        GoodsCacheValue cached = (GoodsCacheValue) goodsCache.getIfPresent(l1Key);
        if (cached != null) {
            if (!cached.isExpired()) {
                return cached.getData();
            }
            //逻辑过期：返回旧数据 + 异步刷新，避免热点 key 击穿
            refreshAsync(l1Key, l2CacheName, type);
            return cached.getData();
        }

        org.springframework.cache.Cache l2Cache = cacheManager.getCache(l2CacheName);
        GoodsCacheValue redisValue = null;
        if (l2Cache != null) {
            try {
                redisValue = l2Cache.get(SimpleKey.EMPTY, GoodsCacheValue.class);
            } catch (Exception e) {
                //历史数据可能是旧格式（裸 List），反序列化失败视为未命中，回源 DB 后覆盖为新格式
                log.warn("缓存反序列化失败，忽略并回源: {}", l2CacheName, e);
            }
        }
        if (redisValue != null) {
            goodsCache.put(l1Key, redisValue);
            if (redisValue.isExpired()) {
                refreshAsync(l1Key, l2CacheName, type);
            }
            return redisValue.getData();
        }

        //两级都未命中，从 DB 加载并双写缓存
        return loadAndPut(l1Key, l2CacheName, type);
    }

    //从 DB 加载并把逻辑过期包装写入 L1/L2，两处都写，保证下次命中不读库
    private List<Goods> loadAndPut(String l1Key, String l2CacheName, IndexConfigTypeEnum type) {
        List<IndexConfig> list = indexConfigMapper.getByType(type.getValue());
        List<Long> ids = list.stream().map(IndexConfig::getGoodsId).collect(Collectors.toList());
        List<Goods> result = ids.isEmpty() ? new ArrayList<>() : goodsMapper.getByIdBatch(ids);

        GoodsCacheValue value = new GoodsCacheValue(System.currentTimeMillis() + LOGICAL_TTL_MS, result);
        goodsCache.put(l1Key, value);
        org.springframework.cache.Cache l2Cache = cacheManager.getCache(l2CacheName);
        if (l2Cache != null) {
            l2Cache.put(SimpleKey.EMPTY, value);
        }
        return result;
    }

    //异步刷新缓存，refreshKeys 去重：同一 key 同时只允许一个刷新任务，防缓存雪崩的请求风暴
    private void refreshAsync(String l1Key, String l2CacheName, IndexConfigTypeEnum type) {
        if (!refreshKeys.add(l1Key)) {
            return;
        }
        asyncExecutor.submit(() -> {
            try {
                loadAndPut(l1Key, l2CacheName, type);
            } catch (Exception e) {
                log.error("逻辑过期异步刷新失败: {}", l1Key, e);
            } finally {
                refreshKeys.remove(l1Key);
            }
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    //重置首页配置：重新计算三个栏目，然后写透两级缓存（覆写逻辑过期包装，避免清空造成的空窗击穿）
    public void resetIndexConfig() {
        resetRecommendGoods();
        resetNewGoods();
        resetPopularGoods();

        //写透：直接读 DB 并覆写两级缓存
        loadAndPut("goods:new", "newCache", IndexConfigTypeEnum.NEW_GOODS);
        loadAndPut("goods:popular", "popularCache", IndexConfigTypeEnum.POPULAR_GOODS);
        loadAndPut("goods:recommend", "recommendCache", IndexConfigTypeEnum.RECOMMEND_GOODS);
        //发布消息，通知其他实例清它们的本地缓存（它们的 L2 已是最新，清 L1 后下次请求命中新 L2）
        stringRedisTemplate.convertAndSend(CacheEvictListener.CACHE_EVICT_TOPIC, CacheEvictListener.INDEX_CONFIG_CACHE);
    }

    @Override
    //重新设置最新商品（首页新品栏）
    public void resetNewGoods() {
        try {
            indexConfigMapper.deleteByType(IndexConfigTypeEnum.NEW_GOODS.getValue());
            List<Goods> latestGoods = goodsMapper.getLatestGoods(10);
            if (latestGoods.isEmpty()) {
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
        } catch (Exception e) {
            log.error("重新设置最新商品失败", e);
            throw new RuntimeException(MessageConstant.RESET_NEW_GOODS_ERROR + ": " + e.getMessage());
        }
    }

    @Override
    //重新设置热销商品（首页热销栏）
    public void resetPopularGoods() {
        try {
            indexConfigMapper.deleteByType(IndexConfigTypeEnum.POPULAR_GOODS.getValue());
            List<Long> hotGoodsIds = shoppingCartMapper.selectTopSellingGoodsIds(10);
            if (hotGoodsIds.isEmpty()) {
                hotGoodsIds = goodsMapper.getLatestGoods(10).stream().map(Goods::getId).collect(Collectors.toList());
            }
            if (hotGoodsIds.isEmpty()) {
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
        } catch (Exception e) {
            log.error("重新设置热销商品失败", e);
            throw new RuntimeException(MessageConstant.RESET_POPULAR_GOODS_ERROR + ": " + e.getMessage());
        }
    }

    @Override
    //重新设置推荐商品（首页推荐栏）
    public void resetRecommendGoods() {
        try {
            indexConfigMapper.deleteByType(IndexConfigTypeEnum.RECOMMEND_GOODS.getValue());
            List<Long> hotGoodsIds = shoppingCartMapper.selectTopSellingGoodsIds(10);
            if (hotGoodsIds.isEmpty()) {
                hotGoodsIds = goodsMapper.getLatestGoods(10).stream().map(Goods::getId).collect(Collectors.toList());
            }
            if (hotGoodsIds.isEmpty()) {
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
        } catch (Exception e) {
            log.error("重新设置推荐商品失败", e);
            throw new RuntimeException(MessageConstant.RESET_RECOMMEND_GOODS_ERROR + ": " + e.getMessage());
        }
    }
}
