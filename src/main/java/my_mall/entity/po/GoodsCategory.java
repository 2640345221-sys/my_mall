package my_mall.entity.po;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class GoodsCategory {
    private  Long id;
    private  Integer level;
    private  Long parentId;
    private  String name;
    private  Integer rank;
    private  LocalDateTime createTime;
    private  LocalDateTime updateTime;
    private  Long createUser;
    private  Long updateUser;
}
