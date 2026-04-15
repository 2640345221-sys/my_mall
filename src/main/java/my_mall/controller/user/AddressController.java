package my_mall.controller.user;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import my_mall.entity.dto.UserAddressDTO;
import my_mall.entity.dto.UserAddressUDTO;
import my_mall.entity.po.UserAddress;
import my_mall.result.Result;
import my_mall.service.AddressService;
import my_mall.utils.TLUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/address")
@Slf4j
public class AddressController {
    @Resource
    private AddressService addressService;

    @PostMapping
    public Result addAddress(@RequestBody UserAddressDTO userAddressDTO) {
        addressService.insert(userAddressDTO);
        return Result.success();
    }

    @GetMapping("/default")
    public Result getDefaultAddress(){
        UserAddress address=addressService.getDefaultAddress();
        return Result.success(address);
    }

    @GetMapping("/{addressId}")
    public Result<UserAddress> getAddress (@PathVariable("addressId") Long addressId){
        UserAddress userAddress=addressService.getAddress(addressId);
        return Result.success(userAddress);
    }

    @DeleteMapping("/{addressId}")
    public Result deleteAddress(@PathVariable("addressId") Long addressId){
        addressService.delete(addressId);
        return Result.success();
    }

    @PutMapping
    public Result updateAddress(@RequestBody UserAddressUDTO userAddressUDTO){
        addressService.update(userAddressUDTO);
        return Result.success();
    }

    @GetMapping
    public Result<List<UserAddress>> getAllAddress(){
        List<UserAddress> list=addressService.getAllAddress();
        return Result.success(list);
    }
}
