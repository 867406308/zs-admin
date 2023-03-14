package com.zs.common.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtil {

    // 秘钥
    private static final String secret = "mygege";
    // 有效时间 毫秒
    private static final Long expire = 60 * 1000L;
    // 用户凭证
    private static final String header = "Authorization";
    // 签发者
    private static final String issuer = "zs.com";

    static Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    /**
     * 生成token签名
     * @param subject
     * @return
     */
    public static String createToken(String subject) {
        Date now = new Date();
        // 过期时间
        Date expireDate = new Date(now.getTime() + expire * 1000);
        //创建Signature SecretKey
//        final SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

        //header参数
        final Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("alg", "HS256");
        headerMap.put("typ", "JWT");

        //生成token
        String token = Jwts.builder()
                .setHeader(headerMap)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .setIssuer(issuer)
                .signWith(key,SignatureAlgorithm.HS256)
                .compact();
        return token;
    }

    /**
     * 解析token
     *
     * @param token token
     * @return
     */
    public static Claims parseToken(String token) {

        Claims claims = null;
        try {
            //创建Signature SecretKey
//            final SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

            claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e) {
            return null;
        }
        return claims;
    }

    /**
     * 判断token是否过期
     *
     * @param expiration
     * @return
     */
    public boolean isExpired(Date expiration) {
        return expiration.before(new Date());
    }

    public static void main(String[] args) {
        String token = createToken("111");
        System.out.println("*******" + token);

        Claims claims = parseToken(token);

        System.out.println(claims);
    }

}
