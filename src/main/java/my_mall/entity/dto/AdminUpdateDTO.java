package my_mall.entity.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class AdminUpdateDTO {
    private String nickName;
    private String password;
    private String username;
}
