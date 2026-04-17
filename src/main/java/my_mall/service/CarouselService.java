package my_mall.service;

import my_mall.entity.dto.CarouselInsertDTO;
import my_mall.entity.dto.CarouselPageDTO;
import my_mall.entity.dto.CarouselUpdateDTO;
import my_mall.entity.po.Carousel;
import my_mall.result.PageResult;

import java.util.List;

public interface CarouselService {
    PageResult page(CarouselPageDTO carouselPageDTO);

    void update(CarouselUpdateDTO carouselUpdateDTO);

    void insert(CarouselInsertDTO carouselInsertDTO);

    Carousel getById(Long id);

    void delete(List<Long> ids);
}
