package my_mall.entity.vo;

import lombok.*;
import my_mall.entity.dto.OrderCartDTO;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class OrderDetailVO {
    private  String orderNo;
    private  Long userId;
    private  Integer totalPrice;
    private  Byte payStatus;
    private  Byte payType;
    private  Byte orderStatus;
    private LocalDateTime payTime;
    private  LocalDateTime createTime;
    private List<OrderCartDTO> orderCartDTO;
}
