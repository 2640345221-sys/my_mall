package my_mall.entity.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class UserPageDTO {
    private Integer pageNumber;
    private Integer pageSize;
    private Byte locked;
}
