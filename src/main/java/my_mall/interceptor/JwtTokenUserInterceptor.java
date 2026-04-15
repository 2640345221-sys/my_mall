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
        if(!(handler instanceof HandlerMethod)){
            return true;
        }


        String token=request.getHeader(jwtProperties.getUserTokenName());
        if (token == null || token.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"msg\":\"未登录\"}");
            response.getWriter().flush();
            return false;
        }
        try{
            log.info("jwt校验token:{}",token);
            Claims claims=JwtUtils.parseJwtToken(token,jwtProperties.getUserSecretKey());
            Long userId=Long.valueOf(claims.get("userId").toString());
            log.info("当前用户id:{}",userId);
            TLUtils.setUserId(userId);
            return true;
        }catch (Exception e){
            log.error("JWT验证失败: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"msg\":\"token无效\"}");
            response.getWriter().flush();
            return false;
        }

    }
}
