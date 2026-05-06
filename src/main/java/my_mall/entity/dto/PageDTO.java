package my_mall.entity.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class PageDTO {
    private Integer pageNumber;
    private Integer pageSize;
}
