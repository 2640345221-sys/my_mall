package my_mall.entity.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class OrderPayDTO {
    private String orderNo;
    private Byte payType;
}
