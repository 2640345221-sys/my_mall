package my_mall.handler;

import lombok.extern.slf4j.Slf4j;
import my_mall.exception.BaseException;
import my_mall.result.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public Result<String> exceptionHandler(BaseException ex) {
        log.error("业务异常：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public Result<String> noResourceHandler(NoResourceFoundException ex) {
        return Result.error("资源不存在");
    }

    @ExceptionHandler(Exception.class)
    public Result<String> exceptionHandler(Exception ex) {
        log.error("系统异常", ex);
        return Result.error("服务器内部错误，请稍后重试");
    }
}
