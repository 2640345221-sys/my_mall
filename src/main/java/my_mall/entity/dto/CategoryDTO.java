package my_mall.entity.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class CategoryDTO {
    private Long id;
    private Byte level;
    private String name;
    private Integer rank;
    private Long parentId;
}
