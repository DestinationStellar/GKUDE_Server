package com.tsingle.gkude_server.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

public class JwtUtil {
    private static final String SECRET = "#GkUde2021ByTsinGle";
    public static final String keyUserId = "uid";

    private static SecretKey generateKey() {
        byte[] encodedKey = Base64.encodeBase64(SECRET.getBytes(StandardCharsets.UTF_8));
        return new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
    }

    public static String createJWT(Map<String, Object> claims, String id, String issuer, String subject, long ttlMillis) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        SecretKey key = generateKey();

        JwtBuilder builder = Jwts.builder().setClaims(claims).setId(id)
                .setIssuedAt(now).setIssuer(issuer).setSubject(subject).signWith(signatureAlgorithm, key);

        if (ttlMillis > 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }
        return builder.compact();
    }

    public static Claims parseJWT(String jwt) {
        SecretKey key = generateKey();
        return Jwts.parser().setSigningKey(key).parseClaimsJws(jwt).getBody();
    }
}
