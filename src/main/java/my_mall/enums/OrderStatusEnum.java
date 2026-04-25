package my_mall.enums;


public enum OrderStatusEnum {
    ORDER_PRE_PAY(0,"待支付"),
    ORDER_PAID(1,"已支付"),
    ORDER_PACKAGED(2,"配货完成"),
    ORDER_EXPRESS(3,"出库成功"),
    ORDER_SUCCESS(4,"交易成功"),
    ORDER_CLOSE_BY_USER(-1,"手动关闭"),
    ORDER_CLOSE_BY_EXPIRED(-2,"超时关闭"),
    ORDER_CLOSE_BY_ADMIN(-3,"商家关闭");

    private Integer status;
    private String name;
    OrderStatusEnum(Integer status,String name){
        this.status=status;
        this.name=name;
    }
    public Integer getStatus() {
        return status;
    }

    public static OrderStatusEnum getOrderStatusEnum(Integer status){
        for(OrderStatusEnum orderStatusEnum:OrderStatusEnum.values()){
            if(orderStatusEnum.status==status){
                return orderStatusEnum;
            }
        }
        return null;
    }

    public static boolean canCancel(Integer status){
        return status==ORDER_PRE_PAY.status;
    }

    public static boolean canCloseByUser(Integer status){
        return status==ORDER_EXPRESS.status;
    }

}
