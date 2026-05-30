package my_mall.entity.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ShoppingCartPageDTO {
    private Integer pageNumber = 1;
    private Integer pageSize = 10;
}
