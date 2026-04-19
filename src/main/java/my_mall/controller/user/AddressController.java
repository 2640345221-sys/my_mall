package my_mall.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import my_mall.entity.dto.UserAddressDTO;
import my_mall.entity.po.UserAddress;
import my_mall.result.Result;
import my_mall.service.AddressService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "用户地址模块")
@RestController
@RequestMapping("/api/user/address")
@Slf4j
public class AddressController {
    @Resource
    private AddressService addressService;

    @Operation(summary = "添加地址")
    @PostMapping
    public Result addAddress(@RequestBody UserAddressDTO userAddressDTO) {
        addressService.insert(userAddressDTO);
        return Result.success();
    }

    @Operation(summary = "获取默认地址")
    @GetMapping("/default")
    public Result getDefaultAddress(){
        UserAddress address=addressService.getDefaultAddress();
        return Result.success(address);
    }

    @Operation(summary = "获取指定地址")
    @GetMapping("/{addressId}")
    public Result<UserAddress> getAddress (@PathVariable("addressId") Long addressId){
        UserAddress userAddress=addressService.getAddress(addressId);
        return Result.success(userAddress);
    }

    @Operation(summary = "删除地址")
    @DeleteMapping("/{addressId}")
    public Result deleteAddress(@PathVariable("addressId") Long addressId){
        addressService.delete(addressId);
        return Result.success();
    }

    @Operation(summary = "更新地址")
    @PutMapping
    public Result updateAddress(@RequestBody UserAddressDTO userAddressDTO){
        addressService.update(userAddressDTO);
        return Result.success();
    }

    @Operation(summary = "获取所有地址")
    @GetMapping
    public Result<List<UserAddress>> getAllAddress(){
        List<UserAddress> list=addressService.getAllAddress();
        return Result.success(list);
    }
}
