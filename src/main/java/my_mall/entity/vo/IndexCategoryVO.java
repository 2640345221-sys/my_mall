package my_mall.entity.vo;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class IndexCategoryVO {
    private Long id;
    private Integer level;
    private String name;
    private Long parentId;
    private List<IndexCategoryVO> children;
}
