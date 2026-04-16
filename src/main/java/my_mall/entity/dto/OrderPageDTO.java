package my_mall.entity.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class OrderPageDTO {
    private Integer pageNumber;
    private Integer pageSize;
    private Integer orderStatus;
}
