package my_mall.entity.po;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Carousel {
    private Long id;
    private String url;
    private String redirectUrl;
    private Integer rank;
    private  LocalDateTime createTime;
    private  LocalDateTime updateTime;
    private  Integer createUser;
    private  Integer updateUser;
}
