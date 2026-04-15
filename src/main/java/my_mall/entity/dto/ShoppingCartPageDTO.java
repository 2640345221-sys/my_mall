package my_mall.entity.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ShoppingCartPageDTO {
    private Integer pageNumber;
    private Integer pageSize;
}
