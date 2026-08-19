package my_mall.mapper;

import my_mall.annotation.OperationFill;
import my_mall.entity.po.UserAddress;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
//收货地址表的增删改查
public interface AddressMapper {
    @OperationFill(fillCreateTime = true,fillUpdateTime = true)
    void insert(UserAddress userAddress);

    @Select("select * from my_mall.user_address where user_id=#{userId} and is_default=1")
    //查用户的默认地址
    UserAddress getDefault(Long userId);

    UserAddress getAddressById(Long addressId);
    @Delete("delete from my_mall.user_address where id=#{addressId}")
    void deleteById(Long addressId);

    @OperationFill(fillUpdateTime = true)
    void update(UserAddress userAddress);

    @Select("select * from my_mall.user_address where user_id=#{userId}")
    List<UserAddress> getByUserId(Long userId);

    //取消该用户所有地址的默认标记
    void cancelDefault(Long userId);
}
