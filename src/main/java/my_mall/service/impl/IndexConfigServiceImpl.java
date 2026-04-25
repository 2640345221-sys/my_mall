package my_mall.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import jakarta.annotation.Resource;
import my_mall.constant.MessageConstant;
import my_mall.entity.dto.IndexConfigDTO;
import my_mall.entity.dto.IndexPageDTO;
import my_mall.entity.po.Goods;
import my_mall.entity.po.IndexConfig;
import my_mall.enums.IndexConfigTypeEnum;
import my_mall.exception.IndexConfigNotExistException;
import my_mall.mapper.GoodsMapper;
import my_mall.mapper.IndexConfigMapper;
import my_mall.result.PageResult;
import my_mall.service.IndexConfigService;
import my_mall.utils.TLUtils;
import org.springframework.transaction.annotation.Transactional;

@Service
public class IndexConfigServiceImpl implements IndexConfigService {
    @Resource
    private IndexConfigMapper indexConfigMapper;
    @Resource
    private GoodsMapper goodsMapper;
    @Override
    public PageResult getPage(IndexPageDTO indexPageDTO) {
        PageResult pageResult = new PageResult();
        PageHelper.startPage(indexPageDTO.getPageNumber(), indexPageDTO.getPageSize());
        Page<IndexConfig> list=indexConfigMapper.getPage(indexPageDTO);
        pageResult.setTotal(list.getTotal());
        pageResult.setTotalPage(list.getPages());
        pageResult.setRecords(list.getResult());
        return pageResult;
    }

    @Override
    public IndexConfig getById(Long id) {
        IndexConfig indexConfig=indexConfigMapper.getById(id);
        if(indexConfig==null){
            throw new IndexConfigNotExistException(MessageConstant.INDEX_CONFIG_NOT_EXIST + "，配置ID：" + id + "，操作用户ID：" + TLUtils.getUserId());
        }
        return indexConfig;
    }

    @Override
    public void delete(List<Long> ids) {
        indexConfigMapper.deleteBatch(ids);
    }

    @Override
    @Transactional
    public void update(IndexConfigDTO indexConfigDTO) {
        IndexConfig indexConfig=indexConfigMapper.getById(indexConfigDTO.getId());
        if(indexConfig==null){
            throw new IndexConfigNotExistException(MessageConstant.INDEX_CONFIG_NOT_EXIST + "，配置ID：" + indexConfigDTO.getId() + "，操作用户ID：" + TLUtils.getUserId());
        }
        BeanUtils.copyProperties(indexConfigDTO,indexConfig);
        indexConfig.setUpdateTime(LocalDateTime.now());
        indexConfig.setUpdateUser(Math.toIntExact(TLUtils.getUserId()));
        indexConfigMapper.update(indexConfig);
    }

    @Override
    public void insert(IndexConfigDTO indexConfigDTO) {
        IndexConfig indexConfig=new IndexConfig();
        BeanUtils.copyProperties(indexConfigDTO,indexConfig);
        indexConfig.setCreateTime(LocalDateTime.now());
        indexConfig.setCreateUser(Math.toIntExact(TLUtils.getUserId()));
        indexConfig.setUpdateTime(LocalDateTime.now());
        indexConfig.setUpdateUser(Math.toIntExact(TLUtils.getUserId()));
        indexConfigMapper.insert(indexConfig);
    }

    @Override
    public List<Goods> getNewGoods() {
        List<IndexConfig> list=indexConfigMapper.getByType(IndexConfigTypeEnum.NEW_GOODS.getValue());
        List<Long> ids=list.stream().map(IndexConfig::getGoodsId).collect(Collectors.toList());
        return goodsMapper.getByIdBatch(ids);
    }

    @Override
    public List<Goods> getPopularGoods() {
        List<IndexConfig> list=indexConfigMapper.getByType(IndexConfigTypeEnum.POPULAR_GOODS.getValue());
        List<Long> ids=list.stream().map(IndexConfig::getGoodsId).collect(Collectors.toList());
        return goodsMapper.getByIdBatch(ids);
    }

    @Override
    public List<Goods> getRecommendGoods() {
        List<IndexConfig> list=indexConfigMapper.getByType(IndexConfigTypeEnum.RECOMMEND_GOODS.getValue());
        List<Long> ids=list.stream().map(IndexConfig::getGoodsId).collect(Collectors.toList());
        List<Goods> goods=goodsMapper.getByIdBatch(ids);
        return goods;
    }
}
