package my_mall.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import my_mall.constant.MessageConstant;
import my_mall.entity.dto.PageDTO;
import my_mall.entity.po.SeckillOrder;
import my_mall.exception.BusinessException;
import my_mall.mapper.SeckillOrderMapper;
import my_mall.result.PageResult;
import my_mall.service.SeckillOrderService;
import my_mall.utils.TLUtils;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
public class SeckillOrderServiceImpl implements SeckillOrderService {

    @Resource
    private SeckillOrderMapper seckillOrderMapper;

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
            throw new BusinessException(MessageConstant.SECKILL_ORDER_NOT_EXIST + "，订单ID：" + id);
        }
        return order;
    }

    @Override
    public SeckillOrder result(Long seckillGoodsId) {
        Long userId = TLUtils.getUserId();
        SeckillOrder order = seckillOrderMapper.getByUserIdAndGoodsId(userId, seckillGoodsId);
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