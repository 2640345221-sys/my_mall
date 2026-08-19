package my_mall.service;

import my_mall.entity.dto.SeckillGoodsPageDTO;
import my_mall.entity.po.SeckillGoods;
import my_mall.result.PageResult;

public interface SeckillGoodsService {
    void save(SeckillGoods seckillGoods);

    void update(SeckillGoods seckillGoods);

    void deleteById(Long id);

    PageResult page(SeckillGoodsPageDTO seckillGoodsPageDTO);

    SeckillGoods getById(Long id);

    void updateStatus(Long id,Integer status);

    void autoUpdateStatus();

    void reconcileStock();
}
