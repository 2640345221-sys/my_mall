package my_mall.entity.po;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Goods implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private  String name;
    private  String intro;
    private  Long categoryId;
    private String coverImg;
    private  String detailContent;
    private  Integer originalPrice;
    private  Integer sellingPrice;
    private  Integer stockNum;
    private   Boolean sellStatus;
    private   LocalDateTime createTime;
    private  LocalDateTime updateTime;
    private   Integer createUser;
    private   Integer updateUser;
}
