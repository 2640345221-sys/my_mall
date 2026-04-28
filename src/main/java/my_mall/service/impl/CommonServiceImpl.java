package my_mall.service.impl;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import my_mall.entity.po.Goods;
import my_mall.entity.po.IndexConfig;
import my_mall.enums.IndexConfigTypeEnum;
import my_mall.mapper.GoodsMapper;
import my_mall.mapper.IndexConfigMapper;
import my_mall.mapper.ShoppingCartMapper;
import my_mall.service.CommonService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CommonServiceImpl implements CommonService {
    @Resource
    private IndexConfigMapper indexConfigMapper;
    @Resource
    private GoodsMapper goodsMapper;
    @Resource
    private ShoppingCartMapper shoppingCartMapper;

    @Transactional
    @Override
    public void resetNewGoods() {
        try {
            log.info("开始重新设置最新商品");

            indexConfigMapper.deleteByType(IndexConfigTypeEnum.NEW_GOODS.getValue());

            // 2. 获取最新的10个商品（按创建时间倒序）
            List<Goods> latestGoods = goodsMapper.getLatestGoods(10);


            if (latestGoods.isEmpty()) {
                log.info("没有找到可用的最新商品");
                return;
            }

            // 3. 创建新的最新商品配置
            List<IndexConfig> newConfigs = latestGoods.stream()
                    .map(goods -> IndexConfig.builder()
                            .name(goods.getName())
                            .type(IndexConfigTypeEnum.NEW_GOODS.getValue())
                            .goodsId(goods.getId())
                            .redirectUrl("/goods/detail/" + goods.getId())
                            .rank(0) // 可以根据需要设置排序
                            .createTime(LocalDateTime.now())
                            .updateTime(LocalDateTime.now())
                            .createUser(0) // 系统操作
                            .updateUser(0) // 系统操作
                            .build())
                    .collect(java.util.stream.Collectors.toList());

            // 4. 批量插入新的配置
            if(newConfigs!=null&&!newConfigs.isEmpty()){
                indexConfigMapper.insertBatch(newConfigs);
            }

            log.info("成功设置 {} 个最新商品", latestGoods.size());
        } catch (Exception e) {
            log.error("重新设置最新商品失败", e);
            throw new RuntimeException("重新设置最新商品失败: " + e.getMessage());
        }
    }

    @Transactional
    @Override
    public void resetPopularGoods() {
        try {
            log.info("开始重新设置热销商品（基于订单销量）");
            // 1. 删除现有热销商品配置
            indexConfigMapper.deleteByType(IndexConfigTypeEnum.POPULAR_GOODS.getValue());

            // 2. 查询销量最高的10个商品ID
            List<Long> hotGoodsIds = shoppingCartMapper.selectTopSellingGoodsIds(10); // 新增 Mapper 方法

            if (hotGoodsIds.isEmpty()) {
                log.info("没有找到热销商品");
                return;
            }

            // 3. 批量查询商品详情（保持顺序）
            List<Goods> hotGoods = goodsMapper.getByIdBatch(hotGoodsIds);

            // 4. 创建 IndexConfig 记录
            List<IndexConfig> configs = hotGoods.stream()
                    .map(goods -> IndexConfig.builder()
                            .name(goods.getName())
                            .type(IndexConfigTypeEnum.POPULAR_GOODS.getValue())
                            .goodsId(goods.getId())
                            .redirectUrl("/goods/detail/" + goods.getId())
                            .rank(0)
                            .createTime(LocalDateTime.now())
                            .updateTime(LocalDateTime.now())
                            .createUser(0)
                            .updateUser(0)
                            .build())
                    .collect(Collectors.toList());

            if(configs!=null&&!configs.isEmpty()){
                indexConfigMapper.insertBatch(configs);
            }
            log.info("成功设置 {} 个热销商品", hotGoods.size());
        } catch (Exception e) {
            log.error("重新设置热销商品失败", e);
            throw new RuntimeException("重新设置热销商品失败: " + e.getMessage());
        }
    }

    @Transactional
    @Override
    public void resetRecommendGoods() {

    }

}
