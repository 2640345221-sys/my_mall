package my_mall.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import jakarta.annotation.Resource;
import my_mall.constant.MessageConstant;
import my_mall.entity.dto.GoodsPageDTO;
import my_mall.entity.dto.GoodsPageSearchDTO;
import my_mall.entity.po.Goods;
import my_mall.entity.po.GoodsCategory;
import my_mall.entity.vo.GoodsDetailVO;
import my_mall.exception.GoodsNotExistException;
import my_mall.mapper.CategoryMapper;
import my_mall.mapper.GoodsMapper;
import my_mall.mapper.IndexConfigMapper;
import my_mall.mapper.SeckillGoodsMapper;
import my_mall.mapper.ShoppingCartMapper;
import my_mall.result.PageResult;
import my_mall.service.GoodsService;
import my_mall.utils.TLUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GoodsServiceImpl implements GoodsService {
    @Resource
    private GoodsMapper goodsMapper;
    @Resource
    private CategoryMapper categoryMapper;
    @Resource
    private ShoppingCartMapper shoppingCartMapper;
    @Resource
    private IndexConfigMapper indexConfigMapper;
    @Resource
    private SeckillGoodsMapper seckillGoodsMapper;
    @Override
    public GoodsDetailVO getGoodsDetail(Long goodsId) {
        Goods goods = goodsMapper.getById(goodsId);
        if (goods == null) {
            throw new GoodsNotExistException(MessageConstant.GOODS_NOT_EXIST + "，商品ID：" + goodsId + "，操作用户ID：" + TLUtils.getUserId());
        }
        return toDetailVO(goods);
    }

    @Override
    public PageResult search(GoodsPageSearchDTO goodsPageSearchDTO) {
        int pageNum = goodsPageSearchDTO.getPageNumber() != null ? goodsPageSearchDTO.getPageNumber() : 1;
        int pageSize = goodsPageSearchDTO.getPageSize() != null ? goodsPageSearchDTO.getPageSize() : 10;
        PageHelper.startPage(pageNum, pageSize);
        Page<Goods> goods = goodsMapper.getPage(goodsPageSearchDTO);
        PageResult pageResult = new PageResult();
        pageResult.setTotal(goods.getTotal());
        pageResult.setTotalPage(goods.getPages());
        pageResult.setRecords(goods.getResult().stream().map(this::toDetailVO).collect(Collectors.toList()));
        return pageResult;
    }

    @Override
    @Transactional
    public void insert(Goods goods) {
        GoodsCategory category = categoryMapper.getById(goods.getCategoryId());
        if (category == null || category.getLevel() != 3) {
            throw new RuntimeException(MessageConstant.CATEGORY_LEVEL_ERROR);
        }

        Goods exist = goodsMapper.getByCategoryAndName(goods.getCategoryId(), goods.getName());
        if (exist != null) {
            throw new RuntimeException(MessageConstant.GOODS_NAME_EXIST);
        }
        goodsMapper.insert(goods);
    }

    @Override
    public GoodsDetailVO getById(Long id) {
        Goods goods = goodsMapper.getById(id);
        if (goods == null) {
            throw new GoodsNotExistException(MessageConstant.GOODS_NOT_EXIST + "，商品ID：" + id + "，操作用户ID：" + TLUtils.getUserId());
        }
        return toDetailVO(goods);
    }

    @Override
    public PageResult page(GoodsPageDTO goodsPageDTO) {
        PageHelper.startPage(goodsPageDTO.getPageNumber(), goodsPageDTO.getPageSize());
        Page<Goods> page = goodsMapper.page(goodsPageDTO);
        PageResult pageResult = new PageResult();
        pageResult.setTotal(page.getTotal());
        pageResult.setTotalPage(page.getPages());
        pageResult.setRecords(page.getResult().stream().map(this::toDetailVO).collect(Collectors.toList()));
        return pageResult;
    }

    private GoodsDetailVO toDetailVO(Goods goods) {
        GoodsDetailVO vo = new GoodsDetailVO();
        BeanUtils.copyProperties(goods, vo);
        return vo;
    }
    @Override
    public void updateStatus(Integer sellStatus, List<Long> ids) {
        goodsMapper.updateStatus(sellStatus,ids);
    }

    @Override
    @Transactional
    public void updateGoods(Goods goods) {
        Goods goodsNew=goodsMapper.getById(goods.getId());
        if(goodsNew==null){
            throw new GoodsNotExistException(MessageConstant.GOODS_NOT_EXIST + "，商品ID：" + goods.getId() + "，操作用户ID：" + TLUtils.getUserId());
        }
        goodsMapper.updateGoods(goods);
    }

    @Override
    @Transactional
    public void deleteGoods(Long id) {
        shoppingCartMapper.deleteByGoodsId(id);
        indexConfigMapper.deleteByGoodsId(id);
        seckillGoodsMapper.deleteByGoodsId(id);
        goodsMapper.deleteBatch(id);
    }
}
