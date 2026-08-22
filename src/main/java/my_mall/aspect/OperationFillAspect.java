package my_mall.aspect;

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
        //未登录（如被拦截器排除的公开路径）时 userId 为空，跳过用户字段，避免把 null 落库
        if(operationFill.fillCreateUser() && userId != null){
            setFieldValue(arg,clazz,"createUser",userId);
        }
        if(operationFill.fillUpdateUser() && userId != null){
            setFieldValue(arg,clazz,"updateUser",userId);
        }
    }

    private void setFieldValue(Object arg, Class<?> clazz, String fieldName, Object value) {
        //非实体参数（如 Integer sellStatus、List ids）没有这些字段，直接跳过
        Field field = findField(clazz, fieldName);
        if (field == null) {
            return;
        }
        try {
            field.setAccessible(true);
            field.set(arg, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    //沿父类向上查找字段，找不到返回 null
    private Field findField(Class<?> clazz, String fieldName) {
        for (Class<?> c = clazz; c != null && c != Object.class; c = c.getSuperclass()) {
            try {
                return c.getDeclaredField(fieldName);
            } catch (NoSuchFieldException ignored) {
            }
        }
        return null;
    }
}
