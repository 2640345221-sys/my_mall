package my_mall.handler;

import lombok.extern.slf4j.Slf4j;
import my_mall.constant.MessageConstant;
import my_mall.exception.BaseException;
import my_mall.result.Result;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.stream.Collectors;

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
        return Result.error(MessageConstant.RESOURCE_NOT_EXIST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<String> methodArgumentNotValidHandler(MethodArgumentNotValidException ex) {
        String msg = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.joining("; "));
        log.warn("参数校验失败：{}", msg);
        return Result.error(msg.isEmpty() ? MessageConstant.PARAM_INVALID : msg);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result<String> httpMessageNotReadableHandler(HttpMessageNotReadableException ex) {
        log.warn("请求体解析失败：{}", ex.getMessage());
        return Result.error(MessageConstant.JSON_PARSE_ERROR);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public Result<String> dataIntegrityViolationHandler(DataIntegrityViolationException ex) {
        log.error("数据完整性违反", ex);
        return Result.error(MessageConstant.DATA_INTEGRITY_ERROR);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result<String> methodNotSupportedHandler(HttpRequestMethodNotSupportedException ex) {
        log.warn("请求方法不支持：{}", ex.getMessage());
        return Result.error(MessageConstant.METHOD_NOT_SUPPORTED);
    }

    @ExceptionHandler(Exception.class)
    public Result<String> exceptionHandler(Exception ex) {
        log.error("系统异常", ex);
        return Result.error(MessageConstant.SERVER_INTERNAL_ERROR);
    }
}
