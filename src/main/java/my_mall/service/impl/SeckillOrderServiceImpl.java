package my_mall.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import my_mall.constant.JudgeConstant;
import my_mall.constant.MessageConstant;
import my_mall.entity.dto.PageDTO;
import my_mall.entity.po.SeckillOrder;
import my_mall.exception.SeckillException;
import my_mall.mapper.SeckillGoodsMapper;
import my_mall.mapper.SeckillOrderMapper;
import my_mall.result.PageResult;
import my_mall.service.SeckillOrderService;
import my_mall.utils.TLUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
public class SeckillOrderServiceImpl implements SeckillOrderService {

    @Resource
    private SeckillOrderMapper seckillOrderMapper;
    @Resource
    private SeckillGoodsMapper seckillGoodsMapper;
    @Resource(name = "stringRedisTemplate")
    private StringRedisTemplate redisTemplate;
    //管理员查询
    @Override
    public PageResult page(PageDTO pageDTO) {
        PageHelper.startPage(pageDTO.getPageNumber(), pageDTO.getPageSize());
        Page<SeckillOrder> page = seckillOrderMapper.pageForAdmin(pageDTO);
        PageResult pageResult = new PageResult();
        pageResult.setTotal(page.getTotal());
        pageResult.setTotalPage(page.getPages());
        pageResult.setRecords(page.getResult());
        return pageResult;
    }


    @Override
    public SeckillOrder getById(Long id) {
        SeckillOrder order = seckillOrderMapper.getById(id);
        if (order == null) {
            throw new SeckillException(MessageConstant.SECKILL_ORDER_NOT_EXIST + "，订单ID：" + id);
        }
        return order;
    }

    @Override
    public SeckillOrder result(Long seckillGoodsId) {
        Long userId = TLUtils.getUserId();
        //先查秒杀失败标记（Redis），失败直接返回 status=-1
        String failKey = "seckill:fail:" + seckillGoodsId + ":" + userId;
        if (Boolean.TRUE.equals(redisTemplate.hasKey(failKey))) {
            return SeckillOrder.builder().status(-1).build();
        }
        var seckillGoods = seckillGoodsMapper.getById(seckillGoodsId);
        if (seckillGoods == null) {
            throw new SeckillException(MessageConstant.SECKILL_GOODS_NOT_EXIST);
        }
        Long goodsId = seckillGoods.getGoodsId();
        SeckillOrder order = seckillOrderMapper.getByUserIdAndGoodsId(userId, goodsId);
        if (order == null) {
            log.info("用户 {} 未参与秒杀商品 {} 或未产生订单", userId, seckillGoodsId);
            return null;
        }
        return order;
    }

    @Override
    public PageResult list(PageDTO pageDTO) {
        Long userId = TLUtils.getUserId();
        PageHelper.startPage(pageDTO.getPageNumber(), pageDTO.getPageSize());
        Page<SeckillOrder> page = seckillOrderMapper.pageForUser(pageDTO, userId);
        PageResult pageResult = new PageResult();
        pageResult.setTotal(page.getTotal());
        pageResult.setTotalPage(page.getPages());
        pageResult.setRecords(page.getResult());
        return pageResult;
    }
}