package my_mall.mapper;

import com.github.pagehelper.Page;
import io.swagger.v3.oas.annotations.Operation;
import my_mall.annotation.OperationFill;
import my_mall.entity.dto.PageDTO;
import my_mall.entity.po.SeckillOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SeckillOrderMapper {

    SeckillOrder getById(Long id);
    @OperationFill(fillCreateTime = true,fillUpdateTime = true)
    void insert(SeckillOrder seckillOrder);

    void update(SeckillOrder seckillOrder);

    void deleteById(Long id);

    Page<SeckillOrder> pageForAdmin(PageDTO pageDTO);

    Page<SeckillOrder> pageForUser(@Param("pageDTO") PageDTO pageDTO, @Param("userId") Long userId);

    SeckillOrder getByUserIdAndGoodsId(Long userId, Long goodsId);
}