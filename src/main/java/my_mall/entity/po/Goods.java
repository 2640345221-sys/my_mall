package my_mall.entity.po;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Goods {
    private Long id;
    private  String name;
    private  String intro;
    private  Long categoryId;
    private String coverImg;
    private  String carousel;
    private  String detailContent;
    private  Integer originalPrice;
    private  Integer sellingPrice;
    private  Integer stockNum;
    private   String tag;
    private   Boolean sellStatus;
    private   LocalDateTime createTime;
    private  LocalDateTime updateTime;
    private   Integer createUser;
    private   Integer updateUser;
}
