package my_mall.service.impl;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import my_mall.constant.JudgeConstant;
import my_mall.entity.dto.SeckillOrderDTO;
import my_mall.entity.dto.StockDeductDTO;
import my_mall.entity.po.*;
import my_mall.enums.IndexConfigTypeEnum;
import my_mall.exception.BaseException;
import my_mall.mapper.*;
import my_mall.service.CommonService;
import my_mall.utils.TLUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
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
    @Resource
    private SeckillGoodsMapper seckillGoodsMapper;
    @Resource
    private SeckillOrderMapper seckillOrderMapper;

    @Override
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
                            .createUser(0)
                            .updateUser(0)
                            .build())
                    .collect(java.util.stream.Collectors.toList());

            if(newConfigs!=null&&!newConfigs.isEmpty()){
                indexConfigMapper.insertBatch(newConfigs);
            }

            log.info("成功设置 {} 个最新商品", latestGoods.size());
        } catch (Exception e) {
            log.error("重新设置最新商品失败", e);
            throw new RuntimeException("重新设置最新商品失败: " + e.getMessage());
        }
    }

    @Override
    public void resetPopularGoods() {
        try {
            log.info("开始重新设置热销商品");

            indexConfigMapper.deleteByType(IndexConfigTypeEnum.POPULAR_GOODS.getValue());

            List<Long> hotGoodsIds = shoppingCartMapper.selectTopSellingGoodsIds(10); // 新增 Mapper 方法

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

    @Override
    public void resetRecommendGoods() {
        //暂定为与推荐商品一致
        try {
            log.info("开始重新设置推荐商品");

            indexConfigMapper.deleteByType(IndexConfigTypeEnum.RECOMMEND_GOODS.getValue());

            List<Long> hotGoodsIds = shoppingCartMapper.selectTopSellingGoodsIds(10); // 新增 Mapper 方法

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
                            .createUser(0)
                            .updateUser(0)
                            .build())
                    .collect(Collectors.toList());

            if(configs!=null&&!configs.isEmpty()){
                indexConfigMapper.insertBatch(configs);
            }
            log.info("成功设置 {} 个推荐商品", hotGoods.size());
        } catch (Exception e) {
            log.error("重新设置推荐商品失败", e);
            throw new RuntimeException("重新设置推荐商品失败: " + e.getMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void createOrderAndReduceDbStock(SeckillOrderDTO seckillOrderDTO) {
        Long seckillGoodsId = seckillOrderDTO.getSeckillGoodsId();
        Long userId= TLUtils.getUserId();
        SeckillGoods seckillGoods= seckillGoodsMapper.getById(seckillGoodsId);
        if(seckillGoods==null){
            throw new BaseException("秒杀商品不存在");
        }
        int rows=seckillGoodsMapper.decreaseStock(seckillGoodsId,seckillOrderDTO.getCount());
        if(rows==0){
            throw new BaseException("数据库库存不足");
        }

        Goods goods=goodsMapper.getById(seckillGoods.getGoodsId());
        int goodsRow=goodsMapper.deductStock(Collections.singletonList(new StockDeductDTO(goods.getId(), seckillOrderDTO.getCount())));
        if(goodsRow==0){
            throw new BaseException("商品库存不足");
        }

        SeckillOrder seckillOrder =SeckillOrder.builder()
                .userId(userId)
                .goodsId(goods.getId())
                .orderId(1L)
                .status(JudgeConstant.ENABLE)
                .build();
        seckillOrderMapper.insert(seckillOrder);

    }
}
