package my_mall.mapper;

import com.github.pagehelper.Page;
import my_mall.annotation.OperationFill;
import my_mall.entity.dto.CarouselPageDTO;
import my_mall.entity.po.Carousel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CarouselMapper {


    Page<Carousel> getPage(CarouselPageDTO carouselMapper);

    @OperationFill(fillUpdateTime = true,fillUpdateUser = true)
    void update(Carousel carousel);
    @OperationFill(fillUpdateTime = true,fillUpdateUser = true,fillCreateUser = true,fillCreateTime = true)
    void insert(Carousel carousel);

    Carousel getById(Long id);

    void deleteBatch(List<Long> ids);
    @Select("select * from my_mall.carousel")
    List<Carousel> getList();
}
