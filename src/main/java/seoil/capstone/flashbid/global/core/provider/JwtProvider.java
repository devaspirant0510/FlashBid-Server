package seoil.capstone.flashbid.global.core.provider;

import com.google.gson.Gson;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import seoil.capstone.flashbid.domain.user.entity.Account;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtProvider {
    private final HashProvider hashUtils;
    @Value("${JWT_SECRET_KEY}")
    private String jwtSecretKey;

    // 액세스토큰 생성
    public String createAccessToken(String userUid, Account account) {
        log.info(jwtSecretKey);
        // 30분
        long ACCESS_TOKEN_EXPIRATION = 1000L * 60 * 30  ;
        SecretKey signedKey = hashUtils.getSignedKey(jwtSecretKey);
        log.info(Base64.getEncoder().encodeToString(signedKey.getEncoded()));
        String jwt = Jwts.builder()
                .setSubject(userUid)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION))
                .claim("nickname", account.getNickname())
                .claim("profileUrl", account.getProfileUrl())
                .claim("email", account.getEmail())
                .claim("id",account.getId())
                .claim("role", account.getUserType())
                .signWith(signedKey, SignatureAlgorithm.HS256)
                .compact();
        log.info(jwt);
        return jwt;
    }

    // 리프레시토큰 생성 헤더에 핵심 uid 정보만 기입
    public String createRefreshToken(String uid, long expiration) {
        SecretKey signedKey = hashUtils.getSignedKey(jwtSecretKey);
        String jwt = Jwts.builder()
                .setSubject(uid)
                .setIssuedAt(new Date())
                .setExpiration(new Date(expiration))
                .signWith(signedKey, SignatureAlgorithm.HS256)
                .compact();
        return jwt;
    }

    // 리프레시토큰 생성 헤더에 핵심 uid 정보만 기입
    public String createRefreshToken(String uid) {
        long REFRESH_TOKEN_EXPIRATION = 60 * 60 * 24 * 14 * 1000L;// 2주
        SecretKey signedKey = hashUtils.getSignedKey(jwtSecretKey);
        String jwt = Jwts.builder()
                .setSubject(uid)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))
                .signWith(signedKey, SignatureAlgorithm.HS256)
                .compact();
        return jwt;
    }

    // jwt Token 의 body 를 파싱하는 메서드
    public <T> T parsingJwtBody(String jwtToken, Class<T> clazz) {
        try {
            // JWT 토큰을 "." 기준으로 분리 1번째 항목이 Body 영역
            String[] splitToken = jwtToken.split("\\.");
            // JWT 페이로드 부분을 Base64 디코딩
            String payload = new String(Base64.getUrlDecoder().decode(splitToken[1]));
            // Gson으로 JSON 문자열을 객체로 변환
            Gson gson = new Gson();
            return gson.fromJson(payload, clazz);
        } catch (Exception e) {
            e.printStackTrace();
            return null;  // 예외 발생 시 null 반환
        }
    }

    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);
        String userUid = claims.getSubject();
        return new UsernamePasswordAuthenticationToken(userUid, null, List.of(new SimpleGrantedAuthority(claims.get("role", String.class))));
    }

    public boolean validateToken(String token) {
        try {

            Jwts.parserBuilder()
                    .setSigningKey(hashUtils.getSignedKey(jwtSecretKey))
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SecurityException e) {
            log.warn("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.warn("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.warn("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.warn("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.warn("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

    public Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(hashUtils.getSignedKey(jwtSecretKey)).build()
                .parseClaimsJws(token)
                .getBody();

    }


}
