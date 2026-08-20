package my_mall.entity.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class UserPageDTO {
    private Integer pageNumber = 1;
    private Integer pageSize = 10;
    private Integer locked;
    private String keyword;
}
