package my_mall.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import jakarta.annotation.Resource;
import my_mall.constant.MessageConstant;
import my_mall.entity.dto.CarouselDTO;
import my_mall.entity.dto.CarouselPageDTO;
import my_mall.entity.po.Carousel;
import my_mall.exception.CarouselNotExistException;
import my_mall.mapper.CarouselMapper;
import my_mall.result.PageResult;
import my_mall.service.CarouselService;
import my_mall.utils.TLUtils;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public void update(CarouselDTO carouselUpdateDTO) {
        Carousel carousel=carouselMapper.getById(carouselUpdateDTO.getId());
        if(carousel==null){
            throw new CarouselNotExistException(MessageConstant.CAROUSEL_NOT_EXIST + "，轮播图ID：" + carouselUpdateDTO.getId() + "，操作用户ID：" + TLUtils.getUserId());
        }
        BeanUtils.copyProperties(carouselUpdateDTO,carousel);
        carousel.setUpdateUser(Math.toIntExact(TLUtils.getUserId()));
        carousel.setUpdateTime(LocalDateTime.now());
        carouselMapper.update(carousel);
    }

    @Override
    public void insert(CarouselDTO carouselDTO) {
        Carousel carousel=new Carousel();
        BeanUtils.copyProperties(carouselDTO,carousel);
        carousel.setCreateUser(Math.toIntExact(TLUtils.getUserId()));
        carousel.setCreateTime(LocalDateTime.now());
        carousel.setUpdateUser(Math.toIntExact(TLUtils.getUserId()));
        carousel.setUpdateTime(LocalDateTime.now());
        carouselMapper.insert(carousel);
    }

    @Override
    public Carousel getById(Long id) {
        Carousel carousel=carouselMapper.getById(id);
        if(carousel==null){
            throw new CarouselNotExistException(MessageConstant.CAROUSEL_NOT_EXIST + "，轮播图ID：" + id + "，操作用户ID：" + TLUtils.getUserId());
        }
        return carousel;
    }

    @Override
    public void delete(List<Long> ids) {
        carouselMapper.deleteBatch(ids);
    }

    @Override
    public List<Carousel> getList() {
        return carouselMapper.getList();
    }
}
