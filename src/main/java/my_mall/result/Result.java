package my_mall.result;

import lombok.Data;

import java.io.Serializable;

@Data
public class Result<T> implements Serializable {
    private Integer code;
    private String message;
    T data;
    public static<T> Result<T> success() {
        Result result = new Result<T>();
        result.setCode(1);
        return result;
    }

    public static<T> Result<T>  success(T data) {
        Result result = new Result<T>();
        result.setCode(1);
        result.setData(data);
        return result;
    }

    public static<T> Result<T>  error(String message) {
        Result result = new Result<T>();
        result.setCode(0);
        result.setMessage(message);
        return result;
    }
}
