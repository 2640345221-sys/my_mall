package my_mall.aspect;

import lombok.SneakyThrows;
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
public class OperationFillAspect {
    //使用了OperationFill注解的方法
    @Pointcut("@annotation(my_mall.annotation.OperationFill)")
    public void operationFillPointcut() {}

    @Around("operationFillPointcut()")
    public Object aroundFill(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        //获取这个地方的注解用来查看要填充哪些参数
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
        //根据参数选择（哪些为True）填充不同的字段
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
        field.set(arg, value);
    }
}
