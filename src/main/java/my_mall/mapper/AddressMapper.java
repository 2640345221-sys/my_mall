package my_mall.mapper;

import my_mall.entity.po.UserAddress;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AddressMapper {
    void insert(UserAddress userAddress);
    @Select("select * from my_mall.user_address where user_id=#{userId} and is_default=1")
    UserAddress getDefault(Long userId);

    UserAddress getAddressById(Long addressId);
    @Delete("delete from my_mall.user_address where id=#{addressId}")
    void deleteById(Long addressId);

    void update(UserAddress userAddress);
    @Select("select * from my_mall.user_address where user_id=#{userId}")
    List<UserAddress> getByUserId(Long userId);
}
