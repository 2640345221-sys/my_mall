package my_mall.enums;

import lombok.Getter;
import lombok.Setter;

//支付方式枚举：0无 1支付宝 2微信
public enum OrderPayTypeEnum {
    NO_PAY(0,"无"),
    ALIPAY_PAY(1,"支付宝支付"),
    WECHAT_PAY(2,"微信支付");
    @Getter
    @Setter
    private Integer value;
    @Getter
    @Setter
    private String Type;
    OrderPayTypeEnum(Integer value, String Type) {
        this.value = value;
        this.Type = Type;
    }
}
