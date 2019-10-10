package com.stamper.yx.common.sys.jwt;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.stamper.yx.common.sys.security.rsa.KeyFactory;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.InvalidKeyException;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtil {
    private static String KEY = "PARAMS";

    /**
     * 解析token中的值,该方法未经过验证,数据安全性不确定
     *
     * @param token
     * @return
     */
    public static AccessToken getJWT(String token) {
        AccessToken accessToken = null;
        try {
            DecodedJWT jwt = JWT.decode(token);
            String entityJson = jwt.getClaim(KEY).asString();
            accessToken = JSONObject.parseObject(entityJson, AccessToken.class);
        } catch (JWTDecodeException e) {
            e.printStackTrace();
        }
        return accessToken;
    }

    /**
     * 解密
     *
     * @param token  令牌
     * @param secret 秘钥
     * @return
     */
    public static AccessToken parseJWT(String token, String secret) {
        AccessToken accessToken = null;
        if (validate(token, secret)) {
            accessToken = getJWT(token);
        }
        return accessToken;
    }

    /**
     * 不要将隐私信息放入（大家都可以获取到）
     *
     * @param entity      要添加的参数
     * @param secret      秘钥(本项目以用户加密后的密码作为秘钥)
     * @param expireTimes 过期时间/毫秒
     * @return
     */
    public static String createJWT(TokenEntity entity, String secret, long expireTimes) throws Exception {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        Date date = new Date(System.currentTimeMillis() + expireTimes);
        String values = JSONObject.toJSONString(entity);
        String token = JWT.create().withClaim(KEY, values).withExpiresAt(date).sign(algorithm);
        return token;
    }

    /**
     * 创建申请单token
     *
     * @param applicationToken 申请单token calm参数列表实例
     * @return
     */
    public static String createJWT2(ApplicationToken applicationToken) {
        if (applicationToken != null) {
            Map<String, Object> claims = new HashMap<>();
            claims.put("application_id", applicationToken.getApplication_id());
            claims.put("status", applicationToken.getStatus());
            claims.put("is_qss", applicationToken.getIs_qss());

            try {
                return Jwts.builder().setClaims(claims).signWith(KeyFactory.getPrivateKey()).compact();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 校验token
     *
     * @param token  令牌
     * @param secret 秘钥
     * @return true:验证成功  false:验证失败
     */
    public static boolean validate(String token, String secret) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm).build();
            verifier.verify(token);
            return true;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (InvalidClaimException ex) {
            ex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) throws Exception {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJQQVJBTVMiOiJ7XCJ0aW1lc3RhbXBcIjowLFwidXNlcklkXCI6OCxcInVzZXJOYW1lXCI6XCJhZG1pblwifSIsImV4cCI6MTU2Nzc2MzkzM30.ERwg8r3ZWE3w9L7nav2dVcrmtw-nr8-SZL_jvPgUWqw";
        String secret = "453334012ee6689df20f82de8fcf653c";
        boolean validate = validate(token, secret);
        System.out.println(validate);
    }
}
