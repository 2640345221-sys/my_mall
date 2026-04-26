package my_mall.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperationLog {
    String module() default "";
    String type() default "";
    String description() default "";
    boolean recordParams() default false;
    boolean recordResult() default false;
}
