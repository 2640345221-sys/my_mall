package my_mall.entity.po;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class UserToken {
    private  Long id;
    private  String token;
    private  LocalDateTime createTime;
    private   LocalDateTime expireTime;
}
