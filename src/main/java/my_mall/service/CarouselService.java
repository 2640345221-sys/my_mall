package my_mall.service;

import my_mall.entity.dto.CarouselDTO;
import my_mall.entity.dto.CarouselPageDTO;
import my_mall.entity.po.Carousel;
import my_mall.result.PageResult;

import java.util.List;

public interface CarouselService {
    PageResult page(CarouselPageDTO carouselPageDTO);

    void update(CarouselDTO carouselUpdateDTO);

    void insert(CarouselDTO carouselDTO);

    Carousel getById(Long id);

    void delete(List<Long> ids);

    List<Carousel> getList();
}
