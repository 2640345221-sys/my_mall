package my_mall.enums;

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

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }
}
