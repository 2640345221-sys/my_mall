package my_mall.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import jakarta.annotation.Resource;
import my_mall.entity.dto.CarouselInsertDTO;
import my_mall.entity.dto.CarouselPageDTO;
import my_mall.entity.dto.CarouselUpdateDTO;
import my_mall.entity.po.Carousel;
import my_mall.mapper.CarouselMapper;
import my_mall.result.PageResult;
import my_mall.service.CarouselService;
import my_mall.utils.TLUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CarouselServiceImpl implements CarouselService {
    @Resource
    private CarouselMapper carouselMapper;

    @Override
    public PageResult page(CarouselPageDTO carouselPageDTO) {
        PageHelper.startPage(carouselPageDTO.getPageNumber(), carouselPageDTO.getPageSize());
        Page<Carousel> page=carouselMapper.getPage(carouselPageDTO);
        PageResult pageResult=new PageResult();
        pageResult.setTotal(page.getTotal());
        pageResult.setTotalPage(page.getPages());
        pageResult.setRecords(page.getResult());
        return pageResult;
    }

    @Override
    public void update(CarouselUpdateDTO carouselUpdateDTO) {
        Carousel carousel=new Carousel();
        BeanUtils.copyProperties(carouselUpdateDTO,carousel);
        carousel.setUpdateUser(Math.toIntExact(TLUtils.getUserId()));
        carousel.setUpdateTime(LocalDateTime.now());
        carouselMapper.update(carousel);
    }

    @Override
    public void insert(CarouselInsertDTO carouselInsertDTO) {
        Carousel carousel=new Carousel();
        BeanUtils.copyProperties(carouselInsertDTO,carousel);
        carousel.setCreateUser(Math.toIntExact(TLUtils.getUserId()));
        carousel.setCreateTime(LocalDateTime.now());
        carousel.setUpdateUser(Math.toIntExact(TLUtils.getUserId()));
        carousel.setUpdateTime(LocalDateTime.now());
        carouselMapper.insert(carousel);
    }

    @Override
    public Carousel getById(Long id) {
        return carouselMapper.getById(id);
    }

    @Override
    public void delete(List<Long> ids) {
        carouselMapper.deleteBatch(ids);
    }
}
