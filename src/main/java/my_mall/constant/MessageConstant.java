package my_mall.constant;

/**
 * 异常消息常量类
 */
public class MessageConstant {
    // 登录相关
    public static final String LOGIN_ERROR="账号或密码错误";
    
    // 商品相关
    public static final String GOODS_NOT_EXIST="商品不存在";
    public static final String GOODS_NOT_SELLING="商品已下架";
    public static final String STOCK_NOT_ENOUGH="商品库存不足";
    public static final String GOODS_NAME_EXIST="该分类下已存在同名商品";
    public static final String CATEGORY_LEVEL_ERROR="必须选择三级分类";
    
    // 分类相关
    public static final String CATEGORY_NOT_EXIST="分类不存在";
    
    // 用户相关
    public static final String USERNAME_EXIST="账号已存在";
    public static final String PASSWORD_EMPTY="密码为空";
    public static final String USER_LOCKED="用户被锁定";
    
    // 地址相关
    public static final String ADDRESS_NOT_EXIST="地址不存在或无权限";
    
    // 购物车相关
    public static final String CART_EMPTY="购物车数据为空";
    public static final String CART_ITEM_NOT_EXIST="购物车项不存在";
    
    // 订单相关
    public static final String ORDER_NOT_EXIST="订单不存在";
    public static final String ORDER_CANNOT_CANCEL="该订单无法取消";
    public static final String ORDER_STATUS_ERROR="订单状态错误";
    public static final String ORDER_CANNOT_CLOSE="当前状态无法关闭";
    public static final String ORDER_CANNOT_PAY="订单状态错误，无法支付";
    public static final String ORDER_CANNOT_CHECK="状态不是已支付，无法配货";
    public static final String ORDER_CANNOT_CHECKOUT="状态不是配货完成，无法出库";
    
    // 权限相关
    public static final String POWER_NOT_ENOUGH="无权操作";
    public static final String POWER_NOT_ENOUGH_ORDER="无权操作此订单";
    public static final String POWER_NOT_ENOUGH_CART="修改权限不足";
    
    // 轮播图相关
    public static final String CAROUSEL_NOT_EXIST="轮播图不存在";
    
    // 首页配置相关
    public static final String INDEX_CONFIG_NOT_EXIST="首页配置不存在";
    
    // 管理员相关
    public static final String ADMIN_NOT_EXIST="不存在该用户";
    public static final String ADMIN_PASSWORD_ERROR="该用户的密码错误";
    
    // 地址相关（新增）
    public static final String ADDRESS_NOT_BELONG="该地址不属于该用户";

}
