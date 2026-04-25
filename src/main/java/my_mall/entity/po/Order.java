package my_mall.entity.po;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Order {
    private  Long id;
    private  String orderNo;
    private  Long userId;
    private  Integer totalPrice;
    private  Integer payStatus;
    private  Integer payType;
    private  Integer orderStatus;
    private  LocalDateTime payTime;
    private  LocalDateTime createTime;
    private  LocalDateTime updateTime;
    private  String extraInfo;
}
