package my_mall.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import jakarta.annotation.PostConstruct;
import java.time.Duration;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import my_mall.constant.MessageConstant;
import my_mall.entity.dto.SeckillGoodsPageDTO;
import my_mall.entity.po.Goods;
import my_mall.entity.po.SeckillGoods;
import my_mall.exception.SeckillException;
import my_mall.mapper.GoodsMapper;
import my_mall.mapper.SeckillGoodsMapper;
import my_mall.result.PageResult;
import my_mall.service.SeckillGoodsService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.time.LocalDateTime;

@Service
@Slf4j
public class SeckillGoodsServiceImpl implements SeckillGoodsService {

    @Resource
    private SeckillGoodsMapper seckillGoodsMapper;
    @Resource
    private GoodsMapper goodsMapper;
    @Resource(name = "stringRedisTemplate")
    private StringRedisTemplate redisTemplate;


    @PostConstruct
    public void initSeckillStock() {
        List<SeckillGoods> activeList = seckillGoodsMapper.selectActiveList();
        for (SeckillGoods goods : activeList) {
            String stockKey = "seckill:stock:" + goods.getId();
            int stockInt=goods.getStockCount().intValue();
            redisTemplate.opsForValue().set(stockKey, String.valueOf(stockInt), Duration.ofHours(2));
            log.info("预热秒杀商品库存，ID: {}, 库存: {}", goods.getId(), goods.getStockCount());
        }
    }
    @Override
    @Transactional
    public void save(SeckillGoods seckillGoods) {
        Goods goods = goodsMapper.getById(seckillGoods.getGoodsId());
        if (goods == null) {
            throw new SeckillException(MessageConstant.GOODS_NOT_EXIST + "，商品ID：" + seckillGoods.getGoodsId());
        }
        if (seckillGoods.getSeckillPrice() == null || seckillGoods.getSeckillPrice() <= 0||seckillGoods.getSeckillPrice() > goods.getSellingPrice()) {
            throw new SeckillException(MessageConstant.SECKILL_PRICE_INVALID);
        }
        if (seckillGoods.getStockCount() == null || seckillGoods.getStockCount() < 0||seckillGoods.getStockCount()>goods.getStockNum()) {
            throw new SeckillException(MessageConstant.SECKILL_STOCK_INVALID);
        }
        LocalDateTime now = LocalDateTime.now();
        if (seckillGoods.getStartTime() == null || seckillGoods.getEndTime() == null ||
                seckillGoods.getStartTime().isAfter(seckillGoods.getEndTime()) ||
                seckillGoods.getEndTime().isBefore(now)) {
            throw new SeckillException(MessageConstant.SECKILL_TIME_INVALID);
        }
        seckillGoods.setStatus(1);
        seckillGoodsMapper.insert(seckillGoods);
        redisTemplate.opsForValue().set("seckill:stock:" + seckillGoods.getId(), String.valueOf(seckillGoods.getStockCount().intValue()), Duration.ofHours(2));
        log.info("新增秒杀商品成功，ID：{}，商品ID：{}", seckillGoods.getId(), seckillGoods.getGoodsId());
    }

    @Override
    @Transactional
    public void update(SeckillGoods seckillGoods) {
        SeckillGoods existing = seckillGoodsMapper.getById(seckillGoods.getId());
        if (existing == null) {
            throw new SeckillException(MessageConstant.SECKILL_GOODS_NOT_EXIST + "，ID：" + seckillGoods.getId());
        }
        if (seckillGoods.getSeckillPrice() != null && seckillGoods.getSeckillPrice() <= 0) {
            throw new SeckillException(MessageConstant.SECKILL_PRICE_INVALID);
        }
        if (seckillGoods.getStockCount() != null && seckillGoods.getStockCount() < 0) {
            throw new SeckillException(MessageConstant.SECKILL_STOCK_INVALID);
        }
        if (seckillGoods.getStartTime() != null && seckillGoods.getEndTime() != null &&
                seckillGoods.getStartTime().isAfter(seckillGoods.getEndTime())) {
            throw new SeckillException(MessageConstant.SECKILL_TIME_INVALID);
        }
        seckillGoodsMapper.update(seckillGoods);
        if (seckillGoods.getStockCount() != null && !seckillGoods.getStockCount().equals(existing.getStockCount())) {
            redisTemplate.opsForValue().set("seckill:stock:" + seckillGoods.getId(), String.valueOf(seckillGoods.getStockCount().intValue()), Duration.ofHours(2));
        }
        log.info("更新秒杀商品成功，ID：{}", seckillGoods.getId());
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        SeckillGoods seckillGoods = seckillGoodsMapper.getById(id);
        if (seckillGoods == null) {
            throw new SeckillException(MessageConstant.SECKILL_GOODS_NOT_EXIST + "，ID：" + id);
        }
        seckillGoodsMapper.deleteById(id);
        redisTemplate.delete("seckill:stock:" + id);
        log.info("删除秒杀商品成功，ID：{}", id);
    }

    @Override
    public PageResult page(SeckillGoodsPageDTO pageDTO) {
        PageHelper.startPage(pageDTO.getPageNumber(), pageDTO.getPageSize());
        Page<SeckillGoods> page = seckillGoodsMapper.pageQuery(pageDTO);
        PageResult pageResult = new PageResult();
        pageResult.setTotal(page.getTotal());
        pageResult.setTotalPage(page.getPages());
        pageResult.setRecords(page.getResult());
        return pageResult;
    }

    @Override
    public SeckillGoods getById(Long id) {
        SeckillGoods seckillGoods = seckillGoodsMapper.getById(id);
        if (seckillGoods == null) {
            throw new SeckillException(MessageConstant.SECKILL_GOODS_NOT_EXIST + "，ID：" + id);
        }
        return seckillGoods;
    }

    @Override
    @Transactional
    public void updateStatus(Long id,Integer status) {
        SeckillGoods existing = seckillGoodsMapper.getById(id);
        if (existing == null) {
            throw new SeckillException(MessageConstant.SECKILL_GOODS_NOT_EXIST + "，ID：" + id);
        }
        seckillGoodsMapper.updateStatus(id, status);
        if (status == 1) {
            redisTemplate.opsForValue().set("seckill:stock:" + id, String.valueOf(existing.getStockCount().intValue()), Duration.ofHours(2));
        } else {
            redisTemplate.delete("seckill:stock:" + id);
        }
        log.info("修改秒杀商品状态成功，ID：{}，新状态：{}", id, status);
    }
}