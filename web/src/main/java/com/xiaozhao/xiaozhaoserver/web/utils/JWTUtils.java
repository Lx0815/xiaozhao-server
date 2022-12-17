package com.xiaozhao.xiaozhaoserver.web.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xiaozhao.xiaozhaoserver.service.exception.BadParameterException;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * @description:
 * @author: Ding
 * @version: 1.0
 * @createTime: 2022-12-17 0:19:16
 * @modify:
 */

@Slf4j
public class JWTUtils {

    public static final String DEFAULT_JWT_SECRET_KEY_ENV = "XIAO_ZHAO_DEFAULT_JWT_SECRET_KEY";

    private static final String JWT_SECRET_KEY = System.getenv(DEFAULT_JWT_SECRET_KEY_ENV);

    private static final int DEFAULT_EXPIRE_TIME = 7 * 24 * 60 * 60;

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static String getToken(String... entries) {
        return getToken(DEFAULT_EXPIRE_TIME, entries);
    }

    /**
     * 生成Token  header.payload.sign
     */
    public static String getToken(int expiresTime, String... entries) {
        if (Objects.isNull(entries) || entries.length == 0 || entries.length % 2 == 1) {
            throw new BadParameterException("entries 参数异常。接收到：" + Arrays.toString(entries));
        }

        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.SECOND, expiresTime); //默认7天过期
        //创建jwt builder
        JWTCreator.Builder builder = JWT.create();

        //payload
        for (int i = 0; i < entries.length; i += 2) {
            builder.withClaim(entries[i], entries[i + 1]);
        }

        return builder.withExpiresAt(instance.getTime())
                .sign(Algorithm.HMAC256(JWT_SECRET_KEY));
    }

    /**
     *  验证token的合法性
     */
    public static boolean verify(String token){
        try {
            //这一行代码就可以起到验证的作用，因为在验证不匹配时它自动会抛出异常
            JWT.require(Algorithm.HMAC256(JWT_SECRET_KEY)).build().verify(token);
        } catch (AlgorithmMismatchException e) {
            log.error("令牌头中声明的算法不等于JWTVerifier中定义的算法。错误的 token：" + token);
            return false;
        } catch (SignatureVerificationException e) {
            log.error("签名无效。错误的 token：" + token);
            return false;
        } catch (TokenExpiredException e) {
            log.error("令牌已过期。错误的 token：" + token);
            return false;
        } catch (InvalidClaimException e) {
            log.error("包含的值与预期值不同。错误的 token：" + token);
            return false;
        }
        return true;
    }

    /**
     * 获取token的信息方法
     */
    @SuppressWarnings("unchecked")
    public static Map<String, String> getTokenInfo(String token){
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(JWT_SECRET_KEY)).build().verify(token);
        String payload = decodedJWT.getPayload();
        String payloadJson = new String(Base64.getUrlDecoder().decode(payload));
        try {
            return objectMapper.readValue(payloadJson, Map.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getId(String token) {
        return getTokenInfo(token).get("id");
    }
}
