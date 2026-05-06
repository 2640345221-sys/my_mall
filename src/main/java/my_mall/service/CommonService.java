package my_mall.service;

import my_mall.entity.dto.SeckillOrderDTO;

public interface CommonService {
    void resetPopularGoods();

    void resetNewGoods();

    void resetRecommendGoods();

    void createOrderAndReduceDbStock(SeckillOrderDTO seckillOrderDTO);

}
