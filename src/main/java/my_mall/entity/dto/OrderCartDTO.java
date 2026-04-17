package my_mall.entity.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class OrderCartDTO {
    private Long goodsId;
    private String goodsName;
    private String coverImg;
    private Integer price;
    private Integer count;
}
