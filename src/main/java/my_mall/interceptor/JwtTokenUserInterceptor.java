package my_mall.interceptor;

import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import my_mall.entity.properties.JwtProperties;
import my_mall.utils.JwtUtils;
import my_mall.utils.TLUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Slf4j
public class JwtTokenUserInterceptor implements HandlerInterceptor {
    @Resource
    private JwtProperties jwtProperties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        TLUtils.remove();

        String token = request.getHeader(jwtProperties.getUserTokenName());

        boolean hasToken = false;
        if (token != null && !token.isEmpty()) {
            try {
                Claims claims = JwtUtils.parseJwtToken(token, jwtProperties.getUserSecretKey());
                Long userId = Long.valueOf(claims.get("userId").toString());
                TLUtils.setUserId(userId);
                log.info("用户id:{}", userId);
                hasToken = true;
            } catch (Exception e) {
                log.error("JWT验证失败: {}", e.getMessage());
            }
        }

        String method = request.getMethod();
        if (!"GET".equalsIgnoreCase(method) && !hasToken) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"msg\":\"请先登录\"}");
            response.getWriter().flush();
            return false;
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        TLUtils.remove();
    }
}
