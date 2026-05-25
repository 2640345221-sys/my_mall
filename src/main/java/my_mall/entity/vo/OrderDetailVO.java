package my_mall.entity.vo;

import lombok.*;
import my_mall.entity.dto.OrderCartDTO;
import my_mall.entity.po.OrderAddress;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class OrderDetailVO {
    private Long id;
    private String orderNo;
    private Long userId;
    private Integer totalPrice;
    private Integer payStatus;
    private Integer payType;
    private Integer orderStatus;
    private LocalDateTime payTime;
    private LocalDateTime createTime;
    private List<OrderCartDTO> orderCartDTO;
    private OrderAddress orderAddress;
}
