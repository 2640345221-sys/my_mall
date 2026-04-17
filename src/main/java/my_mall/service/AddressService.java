package my_mall.service;

import my_mall.entity.dto.UserAddressDTO;
import my_mall.entity.po.UserAddress;

import java.util.List;

public interface AddressService {

    void insert(UserAddressDTO userAddressDTO);

    UserAddress getDefaultAddress();

    UserAddress getAddress(Long addressId);

    void delete(Long addressId);

    void update(UserAddressDTO userAddressUDTO);

    List<UserAddress> getAllAddress();
}
