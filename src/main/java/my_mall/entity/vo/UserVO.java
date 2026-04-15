package my_mall.entity.vo;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class UserVO {
    private String introduceSign;
    private String nickName;
    private String password;
}
