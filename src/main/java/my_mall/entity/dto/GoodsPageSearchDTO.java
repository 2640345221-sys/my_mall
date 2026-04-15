package my_mall.entity.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class GoodsPageSearchDTO {
    String keyword;
    Integer categoryId;
    String orderBy;
    Integer pageNumber;
    Integer pageSize;
}
