package my_mall.utils;



import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtils {
    public static String createJWTToken(String secretKey,long ttlMills, Map<String,Object> claims) {

        SecretKey secret = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Date exp=new Date(nowMillis+ttlMills);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(secret)
                .compact();
    }

    public static Claims parseJwtToken(String token, String secretKey) {
        SecretKey secret = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        return Jwts.parser()
                .verifyWith(secret)
                .build()
                .parseClaimsJws(token).getBody();

    }
}
