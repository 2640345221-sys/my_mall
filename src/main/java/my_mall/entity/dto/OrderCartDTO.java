package my_mall.entity.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class OrderCartDTO {
    private Long goodsId;
    private Long goodsName;
    private String coverImg;
    private Integer price;
    private Long count;
}
