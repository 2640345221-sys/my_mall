package my_mall.aop;

import lombok.extern.slf4j.Slf4j;
import my_mall.annotation.OperationLog;
import my_mall.entity.annotation.OperationLogPO;
import my_mall.utils.JSONDataUtils;
import my_mall.utils.TLUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Aspect
@Component
@Slf4j
public class OperationLogAspect {

    @Pointcut("@annotation(my_mall.annotation.OperationLog)")
    public void operationLogPointcut() {}

    @Around("operationLogPointcut()")
    public Object aroundLog(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        OperationLog operationLog=method.getAnnotation(OperationLog.class);

        OperationLogPO operationLogPO= OperationLogPO.builder()
                .type(operationLog.type())
                .recordParams(operationLog.recordParams())
                .recordResult(operationLog.recordResult())
                .description(operationLog.description())
                .module(operationLog.module())
                .userId(TLUtils.getUserId())
                .createTime(LocalDateTime.now())
                .build();

        if(operationLog.recordParams()){
            String paramJson= JSONDataUtils.formatParams(joinPoint.getArgs());
            operationLogPO.setParams(paramJson);
        }


        Long startTime = System.currentTimeMillis();
        try{

            Object result = joinPoint.proceed();
            if(operationLog.recordResult()){
                String resultJson= JSONDataUtils.formatResult(result);
                operationLogPO.setResult(resultJson);
            }

            operationLogPO.setExecutionTime(System.currentTimeMillis() - startTime);

            log.info("操作日志:\n" +
                            "  模块    : {}\n" +
                            "  类型    : {}\n" +
                            "  描述    : {}\n" +
                            "  参数    : {}\n" +
                            "  结果    : {}\n" +
                            "  耗时    : {} ms\n" +
                            "  用户ID  : {}\n" +
                            "  时间    : {}\n" +
                            "  错误    : {}",
                    operationLogPO.getModule(),
                    operationLogPO.getType(),
                    operationLogPO.getDescription(),
                    operationLogPO.getParams(),
                    operationLogPO.getResult(),
                    operationLogPO.getExecutionTime(),
                    operationLogPO.getUserId(),
                    operationLogPO.getCreateTime(),
                    operationLogPO.getErrorMessage());

            return result;
        }catch (Throwable e){
            operationLogPO.setExecutionTime(System.currentTimeMillis() - startTime);
            operationLogPO.setErrorMessage(e.getMessage());
            if(operationLog.recordResult()){
                operationLogPO.setResult("异常发生，无返回结果");
            }
            log.info("操作失败日志:\n" +
                            "  模块    : {}\n" +
                            "  类型    : {}\n" +
                            "  描述    : {}\n" +
                            "  参数    : {}\n" +
                            "  结果    : {}\n" +
                            "  耗时    : {} ms\n" +
                            "  用户ID  : {}\n" +
                            "  时间    : {}\n" +
                            "  错误    : {}",
                    operationLogPO.getModule(),
                    operationLogPO.getType(),
                    operationLogPO.getDescription(),
                    operationLogPO.getParams(),
                    operationLogPO.getResult(),
                    operationLogPO.getExecutionTime(),
                    operationLogPO.getUserId(),
                    operationLogPO.getCreateTime(),
                    operationLogPO.getErrorMessage());
            throw e;
        }
    }
}
