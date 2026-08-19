package my_mall.enums;

import lombok.Getter;
import lombok.Setter;

//首页配置类型枚举：3热销商品 4最新商品 5推荐商品
public enum IndexConfigTypeEnum {
    POPULAR_GOODS(3,"热销商品"),
    NEW_GOODS(4,"最新商品"),
    RECOMMEND_GOODS(5,"推荐商品"),
    ;
    @Setter
    @Getter
    private Integer value;
    @Setter
    @Getter
    private String indexConfigType;

    IndexConfigTypeEnum(Integer value, String  indexConfigType) {
        this.value = value;
        this.indexConfigType = indexConfigType;
    }
}
