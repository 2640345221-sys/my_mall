package my_mall.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import jakarta.annotation.PostConstruct;
import java.time.Duration;
import jakarta.annotation.Resource;
import my_mall.constant.JudgeConstant;
import my_mall.constant.MessageConstant;
import my_mall.entity.dto.SeckillGoodsPageDTO;
import my_mall.entity.dto.StockDeductDTO;
import my_mall.entity.po.Goods;
import my_mall.entity.po.SeckillGoods;
import my_mall.exception.SeckillException;
import my_mall.mapper.GoodsMapper;
import my_mall.mapper.SeckillGoodsMapper;
import my_mall.mapper.SeckillOrderMapper;
import my_mall.entity.vo.SeckillGoodsVO;
import my_mall.result.PageResult;
import my_mall.service.SeckillGoodsService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.time.LocalDateTime;

@Service
public class SeckillGoodsServiceImpl implements SeckillGoodsService {

    @Resource
    private SeckillGoodsMapper seckillGoodsMapper;
    @Resource
    private GoodsMapper goodsMapper;
    @Resource(name = "stringRedisTemplate")
    private StringRedisTemplate redisTemplate;
    @Resource
    private SeckillOrderMapper seckillOrderMapper;


    @PostConstruct
    public void initSeckillStock() {
        List<SeckillGoods> activeList = seckillGoodsMapper.selectActiveList();
        for (SeckillGoods goods : activeList) {
            String stockKey = "seckill:stock:" + goods.getId();
            int stockInt=goods.getStockCount().intValue();
            redisTemplate.opsForValue().set(stockKey, String.valueOf(stockInt), Duration.ofHours(2));
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
        seckillGoods.setStatus(JudgeConstant.ENABLE);
        seckillGoodsMapper.insert(seckillGoods);
        //预扣商品总库存：把秒杀数量从商品库存里切出来，库存不足则失败
        int rows = goodsMapper.decreaseStock(goods.getId(), seckillGoods.getStockCount());
        if (rows == 0) {
            throw new SeckillException(MessageConstant.SECKILL_STOCK_INVALID);
        }
        redisTemplate.opsForValue().set("seckill:stock:" + seckillGoods.getId(), String.valueOf(seckillGoods.getStockCount().intValue()), Duration.ofHours(2));
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
        //处理秒杀库存变更的商品库存差量
        if (seckillGoods.getStockCount() != null && !seckillGoods.getStockCount().equals(existing.getStockCount())) {
            int diff = seckillGoods.getStockCount() - existing.getStockCount();
            if (diff > 0) {
                //增加秒杀库存：需要再预扣商品库存，商品库存不够则失败
                int rows = goodsMapper.decreaseStock(existing.getGoodsId(), diff);
                if (rows == 0) {
                    throw new SeckillException(MessageConstant.SECKILL_STOCK_INVALID);
                }
            } else {
                //减少秒杀库存：回补商品库存
                goodsMapper.recoverStock(List.of(new StockDeductDTO(existing.getGoodsId(), -diff)));
            }
        }
        seckillGoodsMapper.update(seckillGoods);
        if (seckillGoods.getStockCount() != null && !seckillGoods.getStockCount().equals(existing.getStockCount())) {
            redisTemplate.opsForValue().set("seckill:stock:" + seckillGoods.getId(), String.valueOf(seckillGoods.getStockCount().intValue()), Duration.ofHours(2));
        }
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        SeckillGoods seckillGoods = seckillGoodsMapper.getById(id);
        if (seckillGoods == null) {
            throw new SeckillException(MessageConstant.SECKILL_GOODS_NOT_EXIST + "，ID：" + id);
        }
        seckillGoodsMapper.deleteById(id);
        //删除时回补剩余秒杀库存（先读 Redis 剩余，再删 Redis）
        recoverStockToGoods(seckillGoods);
        redisTemplate.delete("seckill:stock:" + id);
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
            //禁用时回补剩余秒杀库存（先读 Redis 剩余，再删 Redis）
            recoverStockToGoods(existing);
            //剩余已回补商品，本次活动库存归零
            seckillGoodsMapper.updateStock(id, 0);
            redisTemplate.delete("seckill:stock:" + id);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void autoUpdateStatus() {
        //到开始时间且未启用的秒杀，自动启用并预热库存
        List<SeckillGoods> toStartList = seckillGoodsMapper.selectToStart();
        for (SeckillGoods goods : toStartList) {
            seckillGoodsMapper.updateStatus(goods.getId(), JudgeConstant.ENABLE);
            redisTemplate.opsForValue().set("seckill:stock:" + goods.getId(),
                    String.valueOf(goods.getStockCount().intValue()), Duration.ofHours(2));
        }

        //到结束时间且仍启用的秒杀，自动禁用并清理库存
        List<SeckillGoods> toEndList = seckillGoodsMapper.selectToEnd();
        for (SeckillGoods goods : toEndList) {
            seckillGoodsMapper.updateStatus(goods.getId(), JudgeConstant.DISABLE);
            //回补未卖完的秒杀库存到商品总库存
            recoverStockToGoods(goods);
            //剩余已回补商品，本次活动库存归零
            seckillGoodsMapper.updateStock(goods.getId(), 0);
            redisTemplate.delete("seckill:stock:" + goods.getId());
        }
    }

    @Override
    public void reconcileStock() {
        //对账方向：以 DB 库存为权威（已落库扣减），Redis 只是预扣缓存，应等于 DB 减去"未落库的预扣单数"。
        //不能再用 Redis 覆盖 DB，否则高峰期会把"已预扣未落库"的在途部分双重扣减。
        List<SeckillGoods> activeList = seckillGoodsMapper.selectActiveList();
        for (SeckillGoods goods : activeList) {
            String stockKey = "seckill:stock:" + goods.getId();
            String redisStockStr = redisTemplate.opsForValue().get(stockKey);
            if (redisStockStr == null) {
                continue;
            }
            int dbStock = goods.getStockCount();
            //未落库的预扣单数（order_id=0），它们占用了 Redis 库存但还没扣 DB
            int pending = seckillOrderMapper.countPending(goods.getId());
            int expectedRedis = Math.max(0, dbStock - pending);
            if (Integer.parseInt(redisStockStr) != expectedRedis) {
                redisTemplate.opsForValue().set(stockKey, String.valueOf(expectedRedis), Duration.ofHours(2));
            }
        }
    }

    @Override
    public List<SeckillGoodsVO> listActiveForUser() {
        return seckillGoodsMapper.selectActiveListWithGoods();
    }

    //把剩余秒杀库存回补到商品总库存（方案B）：优先用 Redis 剩余库存（实时扣减后的准确值），避免把"还没落库的"也算进去
    private void recoverStockToGoods(SeckillGoods goods) {
        String redisStockStr = redisTemplate.opsForValue().get("seckill:stock:" + goods.getId());
        int remain;
        if (redisStockStr != null) {
            remain = Integer.parseInt(redisStockStr);  // Redis 剩余 = 真正没卖掉的
        } else {
            remain = goods.getStockCount();  // Redis 过期了，退回用数据库值兜底
        }
        if (remain > 0) {
            goodsMapper.recoverStock(List.of(new StockDeductDTO(goods.getGoodsId(), remain)));
        }
    }
}