package my_mall.interceptor;

import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import my_mall.entity.properties.JwtProperties;
import my_mall.utils.JwtUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Slf4j
public class JwtTokenAdminInterceptor implements HandlerInterceptor {
    @Resource
    private JwtProperties jwtProperties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(!(handler instanceof HandlerMethod)){
            return true;
        }

        String token=request.getHeader(jwtProperties.getUserTokenName());
        try{
            log.info("jwt校验token:{}",token);
            Claims claims=JwtUtils.parseJwtToken(token,jwtProperties.getUserSecretKey());
            Long empId=Long.valueOf(claims.get("empId").toString());
            log.info("当前员工id:{}",empId);

            return true;
        }catch (Exception e){
            response.setStatus(401);
            return false;
        }

    }
}
