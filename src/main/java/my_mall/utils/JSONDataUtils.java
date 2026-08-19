package my_mall.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.web.multipart.MultipartFile;

import java.util.stream.Stream;

public class JSONDataUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());
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
            return "参数只有request/response/file等，无法记录";
        }
        return objectMapper.writeValueAsString(filtered);
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
            json = json.substring(0, 2000) + "...";
        }
        return json;
    }
}
