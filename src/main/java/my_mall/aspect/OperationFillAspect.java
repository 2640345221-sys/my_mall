package my_mall.aspect;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import my_mall.annotation.OperationFill;
import my_mall.utils.TLUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDateTime;


@Aspect
@Component
@Slf4j
public class OperationFillAspect {

    @Pointcut("@annotation(my_mall.annotation.OperationFill)")
    public void operationFillPointcut() {}

    @Around("operationFillPointcut()")
    public Object aroundFill(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        OperationFill operationFill = method.getAnnotation(OperationFill.class);

        Object[] args = joinPoint.getArgs();

        for(Object arg : args){
            if(arg==null) continue;
            fillEntityFields(arg,operationFill);
        }
        return joinPoint.proceed();
    }

    private void fillEntityFields(Object arg, OperationFill operationFill) {
        Class<?> clazz = arg.getClass();
        LocalDateTime now=LocalDateTime.now();
        Long userId= TLUtils.getUserId();

        if(operationFill.fillCreateTime()){
            setFieldValue(arg,clazz,"createTime",now);
        }
        if(operationFill.fillUpdateTime()){
            setFieldValue(arg,clazz,"updateTime",now);
        }
        if(operationFill.fillCreateUser()){
            setFieldValue(arg,clazz,"createUser",userId);
        }
        if(operationFill.fillUpdateUser()){
            setFieldValue(arg,clazz,"updateUser",userId);
        }
    }

    @SneakyThrows
    private void setFieldValue(Object arg, Class<?> clazz, String fieldName, Object value) {
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        Class<?> fieldType = field.getType();
        Object finalValue = value;

        if (value != null && !fieldType.isAssignableFrom(value.getClass())) {
            // 类型转换
            if (fieldType == Integer.class && value instanceof Long) {
                finalValue = ((Long) value).intValue();
            } else if (fieldType == Long.class && value instanceof Integer) {
                finalValue = ((Integer) value).longValue();
            } else if (fieldType == String.class) {
                finalValue = value.toString();
            } else {
                log.warn("Cannot set field {} of type {} with value of type {}",
                        fieldName, fieldType.getSimpleName(), value.getClass().getSimpleName());
                return;
            }
        }
        field.set(arg, finalValue);
    }
}
