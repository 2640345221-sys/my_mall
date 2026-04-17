package my_mall.mapper;

import com.github.pagehelper.Page;
import my_mall.entity.dto.CarouselPageDTO;
import my_mall.entity.po.Carousel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CarouselMapper {


    Page<Carousel> getPage(CarouselPageDTO carouselMapper);

    void update(Carousel carousel);

    void insert(Carousel carousel);

    Carousel getById(Long id);

    void deleteBatch(List<Long> ids);
}
