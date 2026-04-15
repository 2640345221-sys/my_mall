package my_mall.entity.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class UserAddressDTO {
    private String city;
    private String detailAddress;
    private Boolean isDefault;
    private String province;
    private String region;
    private String userName;
    private String userPhone;
}
