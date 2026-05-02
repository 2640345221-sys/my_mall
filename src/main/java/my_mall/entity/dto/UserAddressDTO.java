package my_mall.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class UserAddressDTO {
    private Long id;
    private String city;
    private String detailAddress;
    @JsonProperty("isDefault")
    private Boolean isDefault;
    private String province;
    private String region;
    private String username;
    private String userPhone;
}
