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
    private  Integer pageNumber;
    private  Integer pageSize;
}
