package my_mall.service.impl;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import my_mall.constant.MessageConstant;
import my_mall.entity.po.UserAddress;
import my_mall.exception.AddressNotBelongException;
import my_mall.exception.AddressNotExistException;
import my_mall.mapper.AddressMapper;
import my_mall.service.AddressService;
import my_mall.utils.TLUtils;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AddressServiceImpl implements AddressService {
    @Resource
    private AddressMapper addressMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insert(UserAddress userAddress) {
        userAddress.setUserId(TLUtils.getUserId());
        //未传 isDefault 时默认非默认地址，避免 null 落库
        if(userAddress.getIsDefault() == null){
            userAddress.setIsDefault(false);
        }
        //前端可以设置新的地址为默认地址 如果默认则执行下面的代码
        if(Boolean.TRUE.equals(userAddress.getIsDefault())){
            addressMapper.cancelDefault(userAddress.getUserId());
        }
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
        if(!Objects.equals(address.getUserId(), userId)){
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
        if(!Objects.equals(address.getUserId(), userId)){
            throw new AddressNotBelongException(MessageConstant.ADDRESS_NOT_BELONG + "，地址ID：" + addressId + "，地址用户ID：" + address.getUserId() + "，操作用户ID：" + userId);
        }
        addressMapper.deleteById(addressId);
    }

    @SneakyThrows
    @Override
    public void update(UserAddress userAddress) {
        Long userId = TLUtils.getUserId();
        UserAddress address = addressMapper.getAddressById(userAddress.getId());
        if(address==null){
            throw new AddressNotExistException(MessageConstant.ADDRESS_NOT_EXIST + "，地址ID：" + userAddress.getId() + "，操作用户ID：" + userId);
        }
        if(!Objects.equals(address.getUserId(), userId)){
            throw new AddressNotBelongException(MessageConstant.ADDRESS_NOT_BELONG + "，地址ID：" + userAddress.getId() + "，地址用户ID：" + address.getUserId() + "，操作用户ID：" + userId);
        }
        if(Boolean.TRUE.equals(userAddress.getIsDefault())){
            addressMapper.cancelDefault(userId);
        }
        BeanUtils.copyProperties(userAddress,address);
        addressMapper.update(address);
    }

    @Override
    public List<UserAddress> getAllAddress() {
        Long userId = TLUtils.getUserId();
        return addressMapper.getByUserId(userId);
    }
}
