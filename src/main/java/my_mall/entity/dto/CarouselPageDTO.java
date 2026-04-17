package my_mall.entity.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class CarouselPageDTO {
    private Integer pageNumber;
    private Integer pageSize;
}
