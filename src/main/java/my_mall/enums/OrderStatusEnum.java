package my_mall.enums;

import java.util.Objects;

//订单状态枚举：0待支付 1已支付 2配货完成 3出库 4交易成功，负数为各种关闭
public enum OrderStatusEnum {
    ORDER_PRE_PAY(0,"待支付"),
    ORDER_PAID(1,"已支付"),
    ORDER_PACKAGED(2,"配货完成"),
    ORDER_EXPRESS(3,"出库成功"),
    ORDER_SUCCESS(4,"交易成功"),
    ORDER_CLOSE_CONFIRM(-1,"确认订单关闭"),
    ORDER_CLOSE_CANCEL(-3,"取消订单关闭");

    private Integer status;
    private String name;
    OrderStatusEnum(Integer status,String name){
        this.status=status;
        this.name=name;
    }
    public Integer getStatus() {
        return status;
    }

    //只有待支付状态才能取消订单
    public static boolean canCancel(Integer status){
        return Objects.equals(status, ORDER_PRE_PAY.status);
    }

}
