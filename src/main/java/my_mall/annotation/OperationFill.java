package my_mall.annotation;

import java.lang.annotation.*;

@Target(value={ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperationFill {
    boolean fillCreateTime() default false;
    boolean fillUpdateTime() default false;
    boolean fillCreateUser() default false;
    boolean fillUpdateUser() default false;
}
