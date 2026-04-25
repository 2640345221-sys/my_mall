package my_mall.entity.po;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class IndexConfig {
    private  Long id;
    private  String name;
    private  Integer type;
    private Long goodsId;
    private   LocalDateTime createTime;
    private  LocalDateTime updateTime;
    private   Integer createUser;
    private  Integer updateUser;
    private   String redirectUrl;
    private   Integer rank;
}
