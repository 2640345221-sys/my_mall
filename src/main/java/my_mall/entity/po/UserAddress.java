package my_mall.entity.po;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class UserAddress {
    private Long id;
    private Long userId;
    private  String username;
    private  String userPhone;
    private    Boolean isDefault;
    private   String province;
    private   String city;
    private   String region;
    private   String detailAddress;
    private    LocalDateTime createTime;
    private    LocalDateTime updateTime;
}
