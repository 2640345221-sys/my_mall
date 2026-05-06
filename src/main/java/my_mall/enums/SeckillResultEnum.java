package my_mall.enums;

import lombok.Getter;
import lombok.Setter;

public enum SeckillResultEnum {
    SUCCESS(1,"秒杀成功"),
    STOCK_NOT_ENOUGH(-1,"库存不足"),
    REPEAT_SECKILL(-2,"用户已参与过秒杀"),
    UNKNOWN_ERROR(-3,"系统繁忙，请稍后重试");
    @Setter
    @Getter
    private Integer code;
    @Setter
    @Getter
    private String message;
    SeckillResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
