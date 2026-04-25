package my_mall.entity.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class GoodsPageDTO {
    private Integer pageNumber;
    private Integer pageSize;
    private Integer sellStatus;
    private String goodsName;
}
