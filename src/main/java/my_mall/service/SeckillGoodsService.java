package my_mall.service;

import my_mall.entity.dto.SeckillGoodsPageDTO;
import my_mall.entity.po.SeckillGoods;
import my_mall.entity.vo.SeckillGoodsVO;
import my_mall.result.PageResult;

import java.util.List;

public interface SeckillGoodsService {
    void save(SeckillGoods seckillGoods);

    void update(SeckillGoods seckillGoods);

    void deleteById(Long id);

    PageResult page(SeckillGoodsPageDTO seckillGoodsPageDTO);

    SeckillGoods getById(Long id);

    void updateStatus(Long id,Integer status);

    void autoUpdateStatus();

    void reconcileStock();

    //用户端：查进行中的秒杀商品列表
    List<SeckillGoodsVO> listActiveForUser();
}
