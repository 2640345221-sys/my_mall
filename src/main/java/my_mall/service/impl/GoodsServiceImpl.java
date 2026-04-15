package my_mall.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import jakarta.annotation.Resource;
import my_mall.entity.dto.GoodsPageSearchDTO;
import my_mall.entity.po.Goods;
import my_mall.entity.vo.GoodsDetailVO;
import my_mall.mapper.GoodsMapper;
import my_mall.result.PageResult;
import my_mall.service.GoodsService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GoodsServiceImpl implements GoodsService {
    @Resource
    private GoodsMapper goodsMapper;
    @Override
    public GoodsDetailVO getGoodsDetail(Long goodsId) {
        Goods goods=goodsMapper.getById(goodsId);
        GoodsDetailVO goodsDetailVO=new GoodsDetailVO();
        BeanUtils.copyProperties(goods,goodsDetailVO);

        return goodsDetailVO;
    }

    @Override
    public PageResult search(GoodsPageSearchDTO goodsPageSearchDTO) {
        PageHelper.startPage(goodsPageSearchDTO.getPageNumber(), goodsPageSearchDTO.getPageSize());
        Page<Goods> goods=goodsMapper.getPage(goodsPageSearchDTO);
        PageResult pageResult=new PageResult();
        pageResult.setTotal(goods.getTotal());
        pageResult.setTotalPage(goods.getPages());
        List<GoodsDetailVO> goodsDetailVOList=goods.getResult()
                .stream()
                .map(x->{
                    GoodsDetailVO goodsDetailVO=new GoodsDetailVO();
                    BeanUtils.copyProperties(x,goodsDetailVO);
                    return goodsDetailVO;
                }).collect(Collectors.toList());
        pageResult.setRecords(goodsDetailVOList);

        return pageResult;
    }
}
