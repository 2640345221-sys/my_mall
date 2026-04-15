package my_mall.entity.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class CartItemDTO {
    private Integer goodsCount;
    private Long goodsId;
}
