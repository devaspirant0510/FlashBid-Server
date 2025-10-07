package seoil.capstone.flashbid.global.core.provider;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@Slf4j
public class CookieProvider {
    public static final String REFRESH_TOKEN = "refresh_token";
    private final JwtProvider jwtProvider;
    @Value("${MODE:production}")
    private String mode;

    @Deprecated
    public Cookie generateCookie(String key, String value, int time) {
        Cookie cookie = new Cookie(key, value);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setSecure(true);
        cookie.setMaxAge(time);
        return cookie;
    }

    public ResponseCookie generateRefreshTokenCookie(String refreshToken) {
        boolean isProd = mode.equals("production");
        log.info("123mode: {}, isProd: {}", mode, isProd);
        Claims claims = jwtProvider.parseClaims(refreshToken);
        // 만료시간 - 현재시간 = 쿠키 만료시간
        long exp = claims.getExpiration().getTime()/1000;
        int cookieMaxAge = (int)(exp - (System.currentTimeMillis()/1000));

        return ResponseCookie.from(REFRESH_TOKEN, refreshToken)
                .httpOnly(true)
                .secure(isProd) // 운영 환경에서는 true로 설정
                .path("/")
                .maxAge(cookieMaxAge)
                .sameSite(isProd?"None":"Lax")
                .build();
    }
    public Cookie removeCookie(String key) {
        Cookie cookie = new Cookie(key, null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setSecure(true);
        cookie.setMaxAge(0);
        return cookie;
    }

}
