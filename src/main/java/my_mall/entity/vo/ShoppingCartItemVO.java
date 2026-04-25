package my_mall.entity.vo;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ShoppingCartItemVO {
    private Long cartItemId;
    private Integer goodsCount;
    private String coverImg;
    private Long goodsId;
    private String goodsName;
    private Integer sellingPrice;
}
