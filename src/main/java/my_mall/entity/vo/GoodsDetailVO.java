package my_mall.entity.vo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
    private Integer stockNum;
    private Long categoryId;
    private Boolean sellStatus;
}
