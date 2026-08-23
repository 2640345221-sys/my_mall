package my_mall.mapper;

import com.github.pagehelper.Page;
import my_mall.annotation.OperationFill;
import my_mall.entity.dto.PageDTO;
import my_mall.entity.po.SeckillOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
//秒杀订单表的增删改查
public interface SeckillOrderMapper {

    SeckillOrder getById(Long id);
    @OperationFill(fillCreateTime = true,fillUpdateTime = true)
    void insert(SeckillOrder seckillOrder);

    //管理员分页查秒杀订单
    Page<SeckillOrder> pageForAdmin(PageDTO pageDTO);

    //用户分页查自己的秒杀订单
    Page<SeckillOrder> pageForUser(@Param("pageDTO") PageDTO pageDTO, @Param("userId") Long userId);

    //按用户和秒杀活动查秒杀订单（判断是否已秒杀过）
    SeckillOrder getByUserIdAndSeckillGoodsId(Long userId, Long seckillGoodsId);

    //按关联的普通订单id查秒杀订单（取消订单时恢复秒杀库存）
    SeckillOrder getByOrderId(Long orderId);

    //回写秒杀订单关联的普通订单id
    @Update("update my_mall.seckill_order set order_id = #{orderId} where id = #{id}")
    void updateOrderId(@Param("id") Long id, @Param("orderId") Long orderId);

    //更新秒杀订单状态（取消订单时标记为已取消）
    @Update("update my_mall.seckill_order set status = #{status} where id = #{id}")
    void updateStatus(@Param("id") Long id, @Param("status") Integer status);

    //删除未落库的秒杀订单（order_id=0），死信回补用；返回删除行数，0 表示已回补过
    int deletePending(Long userId, Long seckillGoodsId);

    //统计未落库的秒杀预扣单数（order_id=0），对账时用它推算 Redis 应有库存
    int countPending(Long seckillGoodsId);
}