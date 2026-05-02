package my_mall.service;

import my_mall.entity.po.UserAddress;

import java.util.List;

public interface AddressService {

    void insert(UserAddress userAddressDTO);

    UserAddress getDefaultAddress();

    UserAddress getAddress(Long addressId);

    void delete(Long addressId);

    void update(UserAddress userAddressUDTO);

    List<UserAddress> getAllAddress();
}
