package my_mall.service.impl;

import jakarta.annotation.Resource;
import lombok.SneakyThrows;
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
        return addressMapper.getDefault(userId);
    }

    @SneakyThrows
    @Override
    public UserAddress getAddress(Long addressId) {
        Long userId = TLUtils.getUserId();

        UserAddress address= addressMapper.getAddressById(addressId);
        if(address==null){
            throw new Exception("该地址不存在");
        }
        if(address.getUserId()!=userId){
            throw new Exception("该地址不属于该用户");
        }
        return address;
    }

    @SneakyThrows
    @Override
    public void delete(Long addressId) {
        Long userId = TLUtils.getUserId();
        UserAddress address = addressMapper.getAddressById(addressId);
        if(address==null){
            throw new Exception("该地址不存在");
        }
        if(address.getUserId()!=userId){
            throw new Exception("该地址不属于该用户");
        }
        addressMapper.deleteById(addressId);
    }

    @SneakyThrows
    @Override
    public void update(UserAddressDTO userAddressDTO) {
        Long userId = TLUtils.getUserId();
        UserAddress address = addressMapper.getAddressById(userAddressDTO.getId());
        if(address==null){
            throw new Exception("该地址不存在");
        }
        if(address.getUserId()!=userId){
            throw new Exception("该地址不属于该用户");
        }
        BeanUtils.copyProperties(userAddressDTO,address);
        addressMapper.update(address);
    }

    @Override
    public List<UserAddress> getAllAddress() {
        Long userId = TLUtils.getUserId();
        return addressMapper.getByUserId(userId);
    }
}
