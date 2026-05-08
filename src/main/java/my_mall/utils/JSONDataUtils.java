package my_mall.utils;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.web.multipart.MultipartFile;

import java.util.stream.Stream;

public class JSONDataUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    @SneakyThrows
    public static String formatParams(Object[] args) {
        if (args == null || args.length == 0) {
            return "";
        }
        Object[] filtered = Stream.of(args)
                .filter(arg -> !(arg instanceof HttpServletRequest)
                        && !(arg instanceof HttpServletResponse)
                        && !(arg instanceof MultipartFile))
                .toArray();
        if (filtered.length == 0) {
            return "【参数包含request/response/file等，无法记录】";
        }
        // 如果只有一个参数，直接序列化该参数；多个参数则序列化为数组
        Object toSerialize = filtered.length == 1 ? filtered[0] : filtered;
        return objectMapper.writeValueAsString(toSerialize);
    }

    /**
     * 格式化返回结果：避免超大对象/循环引用
     */
    @SneakyThrows
    public static String formatResult(Object result) {
        if (result == null) {
            return "null";
        }
        String json = objectMapper.writeValueAsString(result);
        if (json.length() > 2000) {
            json = json.substring(0, 2000) + "...(truncated)";
        }
        return json;
    }
}
