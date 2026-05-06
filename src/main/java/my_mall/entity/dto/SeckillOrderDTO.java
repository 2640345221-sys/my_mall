package my_mall.entity.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class SeckillOrderDTO {
    private Long seckillGoodsId;
    private Long addressId;
    private Integer count;
}
