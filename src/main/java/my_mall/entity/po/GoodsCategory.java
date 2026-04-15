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
    private  Byte level;
    private Long parentId;
    private  String name;
    private  Integer rank;
    private  Boolean isDeleted;
    private   LocalDateTime createTime;
    private   LocalDateTime updateTime;
    private  Integer createUser;
    private   Integer updateUser;
}
