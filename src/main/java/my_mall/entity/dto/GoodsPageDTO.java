package my_mall.entity.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class GoodsPageDTO {
    private Integer pageNumber = 1;
    private Integer pageSize = 10;
    private Integer sellStatus;
    private String goodsName;
}
