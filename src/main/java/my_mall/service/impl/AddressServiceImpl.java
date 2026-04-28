package my_mall.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import my_mall.mapper.UserMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import my_mall.constant.MessageConstant;
import my_mall.entity.dto.UserAddressDTO;
import my_mall.entity.po.UserAddress;
import my_mall.exception.AddressNotBelongException;
import my_mall.exception.AddressNotExistException;
import my_mall.mapper.AddressMapper;
import my_mall.service.AddressService;
import my_mall.utils.TLUtils;

@Service
public class AddressServiceImpl implements AddressService {
    @Resource
    private AddressMapper addressMapper;

    @Override
    public void insert(UserAddressDTO userAddressDTO) {
        UserAddress userAddress = new UserAddress();
        BeanUtils.copyProperties(userAddressDTO,userAddress);
        userAddress.setUserId(TLUtils.getUserId());
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
            throw new AddressNotExistException(MessageConstant.ADDRESS_NOT_EXIST + "，地址ID：" + addressId + "，操作用户ID：" + userId);
        }
        if(address.getUserId()!=userId){
            throw new AddressNotBelongException(MessageConstant.ADDRESS_NOT_BELONG + "，地址ID：" + addressId + "，地址用户ID：" + address.getUserId() + "，操作用户ID：" + userId);
        }
        return address;
    }

    @SneakyThrows
    @Override
    public void delete(Long addressId) {
        Long userId = TLUtils.getUserId();
        UserAddress address = addressMapper.getAddressById(addressId);
        if(address==null){
            throw new AddressNotExistException(MessageConstant.ADDRESS_NOT_EXIST + "，地址ID：" + addressId + "，操作用户ID：" + userId);
        }
        if(address.getUserId()!=userId){
            throw new AddressNotBelongException(MessageConstant.ADDRESS_NOT_BELONG + "，地址ID：" + addressId + "，地址用户ID：" + address.getUserId() + "，操作用户ID：" + userId);
        }
        addressMapper.deleteById(addressId);
    }

    @SneakyThrows
    @Override
    public void update(UserAddressDTO userAddressDTO) {
        Long userId = TLUtils.getUserId();
        UserAddress address = addressMapper.getAddressById(userAddressDTO.getId());
        if(address==null){
            throw new AddressNotExistException(MessageConstant.ADDRESS_NOT_EXIST + "，地址ID：" + userAddressDTO.getId() + "，操作用户ID：" + userId);
        }
        if(address.getUserId()!=userId){
            throw new AddressNotBelongException(MessageConstant.ADDRESS_NOT_BELONG + "，地址ID：" + userAddressDTO.getId() + "，地址用户ID：" + address.getUserId() + "，操作用户ID：" + userId);
        }
        BeanUtils.copyProperties(userAddressDTO,address);
        address.setUpdateTime(LocalDateTime.now());
        addressMapper.update(address);
    }

    @Override
    public List<UserAddress> getAllAddress() {
        Long userId = TLUtils.getUserId();
        return addressMapper.getByUserId(userId);
    }
}
