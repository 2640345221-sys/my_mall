package my_mall.service;

import my_mall.entity.dto.SeckillOrderDTO;
import my_mall.enums.SeckillResultEnum;

public interface SeckillService {
    SeckillResultEnum seckillWork(SeckillOrderDTO seckillOrderDTO);
}
