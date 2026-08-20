package my_mall.entity.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class GoodsPageSearchDTO {
    private String keyword;
    private  Integer categoryId;
    private  String orderBy;
    private  String sort;
    private Integer pageNumber = 1;
    private Integer pageSize = 10;
}
