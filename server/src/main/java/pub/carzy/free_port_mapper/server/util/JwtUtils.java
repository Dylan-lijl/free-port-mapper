package pub.carzy.free_port_mapper.server.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.Data;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * @author admin
 */
@Data
public class JwtUtils {
    private String sign;

    /**
     * 获取token
     *
     * @param map  携带部分数据
     * @param time 单位秒
     * @return
     */
    public String getToken(Map<String, Object> map, Integer time) {
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.SECOND, time);
        // 创建jwt builder
        JWTCreator.Builder builder = JWT.create();
        map.forEach((k,v)-> builder.withClaim(k,v.toString()));
        return builder.withExpiresAt(instance.getTime())
                .sign(Algorithm.HMAC256(sign));
    }

    /**
     * 验证token合法性
     *
     * @param token
     */
    public boolean verify(String token) {
        try {
            JWT.require(Algorithm.HMAC256(sign)).build().verify(token);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    /**
     * 获取token中的payload
     *
     * @param token
     * @return
     */
    public DecodedJWT getTokenInfo(String token) {
        if (verify(token)){
            return JWT.require(Algorithm.HMAC256(sign)).build().verify(token);
        }
        return null;
    }

    /**
     * 判断 token 是否过期
     * true 未过期
     * false 已过期
     */
    public boolean isTokenExpired(String token) {
        if (verify(token)){
            DecodedJWT verify = JWT.require(Algorithm.HMAC256(sign)).build().verify(token);
            return verify.getExpiresAt().after(new Date());
        }
        return false;
    }
}
