package my_mall.entity.po;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Admin {
    private Long id;
    private String username;
    private String password;
    private String nickName;
    private Boolean locked;
}
