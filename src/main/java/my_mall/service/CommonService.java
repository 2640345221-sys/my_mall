package my_mall.service;

import my_mall.entity.dto.SeckillOrderDTO;
import org.springframework.transaction.annotation.Transactional;

public interface CommonService {
    void resetPopularGoods();

    void resetNewGoods();

    void resetRecommendGoods();


    @Transactional(rollbackFor = Exception.class)
    void createOrderAndReduceDbStock(SeckillOrderDTO seckillOrderDTO);
}
