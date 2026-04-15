package my_mall.service;

import my_mall.entity.dto.GoodsPageSearchDTO;
import my_mall.entity.vo.GoodsDetailVO;
import my_mall.result.PageResult;

public interface GoodsService {
    GoodsDetailVO getGoodsDetail(Long goodsId);

    PageResult search(GoodsPageSearchDTO goodsPageSearchDTO);
}
