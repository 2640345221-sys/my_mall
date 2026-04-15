package my_mall.entity.po;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class OrderItem {
    private Long id;
    private Long orderId;
    private  Long goodsId;
    private  String goodsName;
    private  String coverImg;
    private  Integer price;
    private  Integer count;
    private   LocalDateTime createTime;
}
