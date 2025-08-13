package com.example.gyeol.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;           // Base64 디코더
import io.jsonwebtoken.security.Keys;        // HS256 키 생성
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

/**
 * JWT 발급/검증 유틸
 * - 토큰 내용:
 *   - sub: 사용자명(보통 username)
 *   - jti: 사용자 PK 등 고유 ID (필터에서 DB조회에 사용)
 *   - iat: 발급시각
 *   - exp: 만료시각
 */
@Component
public class JwtUtil {

    private final SecretKey key;            // HS256 서명용 비밀키
    private final long accessValidityMs;    // yml의 jwt.access-token-validity-ms

    public JwtUtil(
            @Value("${jwt.secret}") String secretBase64,
            @Value("${jwt.access-token-validity-ms}") long accessValidityMs
    ) {

        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretBase64));
        this.accessValidityMs = accessValidityMs;
    }

    /**
     * 액세스 토큰 발급
     */
    public String generateToken(String subject, String jti) {
        long now = System.currentTimeMillis();
        Date iat = new Date(now);                    // 발급시각
        Date exp = new Date(now + accessValidityMs); // 만료시각

        return Jwts.builder()
                .setSubject(subject)
                .setId(jti)
                .setIssuedAt(iat)
                .setExpiration(exp)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 토큰 파싱, 유효성 검증(서명/만료)
     * - 실패 시 예외 발생, 필터에서 401 처리
     */
    public Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)   // 서명/만료 검증
                .getBody();
    }
}
