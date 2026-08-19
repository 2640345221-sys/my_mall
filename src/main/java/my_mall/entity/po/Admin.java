package my_mall.entity.po;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Admin {
    private Long id;
    private String username;
    @JsonIgnore
    private String password;
    private String nickName;
    private Boolean locked;
}
