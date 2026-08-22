package my_mall.constant;

/**
 * 异常消息常量类
 */
public class MessageConstant {
    public static final String LOGIN_ERROR="账号或密码错误";
    public static final String USERNAME_NOT_EXIST="用户名不存在";
    public static final String GOODS_NOT_EXIST="商品不存在";
    public static final String GOODS_NOT_SELLING="商品已下架";
    public static final String STOCK_NOT_ENOUGH="商品库存不足";
    public static final String GOODS_NAME_EXIST="该分类下已存在同名商品";
    public static final String CATEGORY_LEVEL_ERROR="必须选择三级分类";
    public static final String CATEGORY_NOT_EXIST="分类不存在";
    public static final String USERNAME_EXIST="账号已存在";
    public static final String PASSWORD_EMPTY="密码为空";
    public static final String USER_LOCKED="用户被锁定";
    public static final String ADDRESS_NOT_EXIST="地址不存在或无权限";
    public static final String CART_EMPTY="购物车数据为空";
    public static final String CART_ITEM_NOT_EXIST="购物车项不存在";
    public static final String ORDER_NOT_EXIST="订单不存在";
    public static final String ORDER_CANNOT_CANCEL="该订单无法取消";
    public static final String ORDER_STATUS_ERROR="订单状态错误";
    public static final String ORDER_CANNOT_CLOSE="当前状态无法关闭";
    public static final String ORDER_CANNOT_PAY="订单状态错误，无法支付";
    public static final String ORDER_CANNOT_CHECK="状态不是已支付，无法配货";
    public static final String ORDER_CANNOT_CHECKOUT="状态不是配货完成，无法出库";
    public static final String POWER_NOT_ENOUGH="无权操作";
    public static final String POWER_NOT_ENOUGH_ORDER="无权操作此订单";
    public static final String POWER_NOT_ENOUGH_CART="修改权限不足";
    public static final String INDEX_CONFIG_NOT_EXIST="首页配置不存在";
    public static final String ADMIN_NOT_EXIST="不存在该用户";
    public static final String ADMIN_PASSWORD_ERROR="该用户的密码错误";
    public static final String ADDRESS_NOT_BELONG="该地址不属于该用户";
    public static final String SECKILL_ORDER_NOT_EXIST = "秒杀订单不存在";
    public static final String SECKILL_PRICE_INVALID = "秒杀价格不合规";
    public static final String SECKILL_STOCK_INVALID = "秒杀库存不合规";
    public static final String SECKILL_TIME_INVALID = "秒杀开始时间必须早于结束时间，且结束时间不能早于当前时间";
    public static final String SECKILL_GOODS_NOT_EXIST = "秒杀商品不存在";
    public static final String ORDER_TIMEOUT_HANDLE_ERROR = "超时订单处理失败";
    public static final String ORDER_AUTO_CONFIRM_ERROR = "自动确认完成订单失败";
    public static final String FILE_EMPTY = "文件为空";
    public static final String FILE_NAME_EMPTY = "文件名为空";
    public static final String FILE_EXTENSION_UNRECOGNIZED = "无法识别文件扩展名";
    public static final String RESET_NEW_GOODS_ERROR = "重新设置最新商品失败";
    public static final String RESET_POPULAR_GOODS_ERROR = "重新设置热销商品失败";
    public static final String RESET_RECOMMEND_GOODS_ERROR = "重新设置推荐商品失败";
    public static final String DB_STOCK_NOT_ENOUGH = "数据库库存不足";
    public static final String SECKILL_STOCK_NOT_ENOUGH = "库存不足";
    public static final String RESOURCE_NOT_EXIST = "资源不存在";
    public static final String SERVER_INTERNAL_ERROR = "服务器内部错误，请稍后重试";
    public static final String METHOD_NOT_SUPPORTED = "请求方法不支持";
    public static final String USERNAME_EMPTY = "用户名不能为空";
    public static final String USERNAME_TOO_LONG = "用户名长度不能超过11个字符";
    public static final String PARAM_INVALID = "参数不合法";
    public static final String JSON_PARSE_ERROR = "请求体格式错误，请检查JSON格式";
    public static final String DATA_INTEGRITY_ERROR = "数据不合法：可能存在超长字段或违反唯一约束";
}
