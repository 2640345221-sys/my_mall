package my_mall.entity.po;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class User {
    private  Long id;
    private  String nickName;
    private String loginName;
    @JsonIgnore
    private  String password;
    private   String introduceSign;
    private  Boolean locked;
    private   LocalDateTime createTime;
}
