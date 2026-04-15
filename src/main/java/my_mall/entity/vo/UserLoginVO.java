package my_mall.entity.vo;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class UserLoginVO {
    private Long id;
    private String nickName;
    private String loginName;
    private String token;
}
