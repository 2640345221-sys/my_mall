package my_mall.entity.po;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ShoppingCart {
    private  Long id;
    private Long userId;
    private  Long goodsId;
    private  Integer goodsCount;
    private   LocalDateTime updateTime;
    private   LocalDateTime createTime;
}
