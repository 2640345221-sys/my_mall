package my_mall.entity.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class SeckillGoodsPageDTO {
    private Integer pageNumber;
    private Integer pageSize;
    private Integer status;
    private String goodsName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
