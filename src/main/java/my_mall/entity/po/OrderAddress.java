package my_mall.entity.po;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class OrderAddress {
    private Long id;
    private String username;
    private String userPhone;
    private String province;
    private String city;
    private String region;
    private String detailAddress;
    private Long orderId;
}
