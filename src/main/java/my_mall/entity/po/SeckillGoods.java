package my_mall.entity.po;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class SeckillGoods {
    private Long id;
    private Long goodsId;
    private Integer seckillPrice;
    private Double stockCount;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer status;
}
