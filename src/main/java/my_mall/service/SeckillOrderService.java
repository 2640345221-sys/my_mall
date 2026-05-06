package my_mall.service;

import my_mall.entity.dto.PageDTO;
import my_mall.entity.po.SeckillOrder;
import my_mall.result.PageResult;

public interface SeckillOrderService {
    PageResult page(PageDTO pageDTO);

    SeckillOrder getById(Long id);

    SeckillOrder result(Long id);

    PageResult list(PageDTO pageDTO);
}
