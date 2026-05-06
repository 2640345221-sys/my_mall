package my_mall.entity.po;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class SeckillOrder {
    private Long id;
    private Long userId;
    private Long goodsId;
    private Long orderId;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
