package my_mall.service.impl;

import jakarta.annotation.Resource;
import my_mall.entity.dto.UserAddressDTO;
import my_mall.entity.po.UserAddress;
import my_mall.mapper.AddressMapper;
import my_mall.service.AddressService;
import my_mall.utils.TLUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {
    @Resource
    private AddressMapper addressMapper;

    @Override
    public void insert(UserAddressDTO userAddressDTO) {
        UserAddress userAddress = new UserAddress();
        BeanUtils.copyProperties(userAddressDTO,userAddress);
        userAddress.setUserId(TLUtils.getUserId());
        userAddress.setCreateTime(LocalDateTime.now());
        userAddress.setUpdateTime(LocalDateTime.now());
        addressMapper.insert(userAddress);
    }

    @Override
    public UserAddress getDefaultAddress() {
        Long userId = TLUtils.getUserId();
        UserAddress userAddress = addressMapper.getDefault(userId);
        return userAddress;
    }

    @Override
    public UserAddress getAddress(Long addressId) {
        UserAddress userAddress = addressMapper.getAddressById(addressId);
        return userAddress;
    }

    @Override
    public void delete(Long addressId) {
        addressMapper.deleteById(addressId);
    }

    @Override
    public void update(UserAddressDTO userAddressDTO) {
        UserAddress userAddress =new UserAddress();
        BeanUtils.copyProperties(userAddressDTO,userAddress);
        addressMapper.update(userAddress);
    }

    @Override
    public List<UserAddress> getAllAddress() {
        Long userId = TLUtils.getUserId();
        List<UserAddress> list=addressMapper.getByUserId(userId);
        return list;
    }
}
