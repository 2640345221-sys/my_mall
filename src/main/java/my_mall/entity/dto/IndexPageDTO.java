package my_mall.entity.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class IndexPageDTO {
    private Integer pageNumber;
    private Integer pageSize;
    private Integer type;
}
