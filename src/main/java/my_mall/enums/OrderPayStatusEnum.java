package my_mall.enums;

//订单支付状态枚举：0未支付 1已支付 -1支付失败
public enum OrderPayStatusEnum {
    NO_PAY(0,"未支付"),
    PAID(1,"已支付"),
    PAY_FAIL(-1,"支付失败"),
    ;
    private Integer value;
    private String payStatus;
    OrderPayStatusEnum(Integer value, String payStatus) {
        this.value = value;
        this.payStatus = payStatus;
    }

    public Integer getValue() {
        return value;
    }
}
