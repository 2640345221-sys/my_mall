package my_mall.service;

import my_mall.entity.dto.GoodsPageDTO;
import my_mall.entity.dto.GoodsPageSearchDTO;
import my_mall.entity.po.Goods;
import my_mall.entity.vo.GoodsDetailVO;
import my_mall.result.PageResult;

import java.util.List;

public interface GoodsService {
    GoodsDetailVO getGoodsDetail(Long goodsId);

    PageResult search(GoodsPageSearchDTO goodsPageSearchDTO);

    void insert(Goods goods);

    GoodsDetailVO getById(Long id);

    PageResult page(GoodsPageDTO goodsPageDTO);

    void updateStatus(Integer sellStatus, List<Long> ids);

    void updateGoods(Goods goods);

    void deleteGoods(Long ids);
}
