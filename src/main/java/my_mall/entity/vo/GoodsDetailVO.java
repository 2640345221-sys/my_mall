package my_mall.entity.vo;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder

public class GoodsDetailVO {
    private List<String> carousel;
    private String coverImg;
    private String detailContent;
    private  Long id;
    private  String intro;
    private  String name;
    private  Integer originalPrice;
    private  Integer sellingPrice;
    private  String tag;
}
