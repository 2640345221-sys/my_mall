package my_mall.controller.user;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import my_mall.annotation.OperationLog;
import my_mall.entity.dto.UserAddressDTO;
import my_mall.entity.po.UserAddress;
import my_mall.result.Result;
import my_mall.service.AddressService;

@Tag(name = "用户地址模块")
@RestController
@RequestMapping("/api/user/address")
@Slf4j
public class AddressController {
    @Resource
    private AddressService addressService;

    @Operation(summary = "添加地址")
    @OperationLog(module = "用户地址模块", type = "新增", description = "添加地址",
            recordParams = true, recordResult = true)
    @PostMapping
    public Result addAddress(@RequestBody UserAddressDTO userAddressDTO) {
        addressService.insert(userAddressDTO);
        return Result.success();
    }

    @Operation(summary = "获取默认地址")
    @OperationLog(module = "用户地址模块", type = "查询", description = "获取默认地址",
            recordParams = true, recordResult = true)
    @GetMapping("/default")
    public Result getDefaultAddress(){
        UserAddress address=addressService.getDefaultAddress();
        return Result.success(address);
    }

    @Operation(summary = "获取指定地址")
    @OperationLog(module = "用户地址模块", type = "查询", description = "获取指定地址",
            recordParams = true, recordResult = true)
    @GetMapping("/{addressId}")
    public Result<UserAddress> getAddress (@PathVariable("addressId") Long addressId){
        UserAddress userAddress=addressService.getAddress(addressId);
        return Result.success(userAddress);
    }

    @Operation(summary = "删除地址")
    @OperationLog(module = "用户地址模块", type = "删除", description = "删除地址",
            recordParams = true, recordResult = true)
    @DeleteMapping("/{addressId}")
    public Result deleteAddress(@PathVariable("addressId") Long addressId){
        addressService.delete(addressId);
        return Result.success();
    }

    @Operation(summary = "更新地址")
    @OperationLog(module = "用户地址模块", type = "更新", description = "更新地址",
            recordParams = true, recordResult = true)
    @PutMapping
    public Result updateAddress(@RequestBody UserAddressDTO userAddressDTO){
        addressService.update(userAddressDTO);
        return Result.success();
    }

    @Operation(summary = "获取所有地址")
    @OperationLog(module = "用户地址模块", type = "查询", description = "获取所有地址",
            recordParams = true, recordResult = true)
    @GetMapping
    public Result<List<UserAddress>> getAllAddress(){
        List<UserAddress> list=addressService.getAllAddress();
        return Result.success(list);
    }
}
