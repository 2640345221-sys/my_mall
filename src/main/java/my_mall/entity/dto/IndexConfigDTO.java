package my_mall.entity.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class IndexConfigDTO {
    private Long id;
    private String name;
    private Integer rank;
    private Byte type;
    private Long goodsId;
}
