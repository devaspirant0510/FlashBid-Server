package seoil.capstone.flashbid.global.core.provider;

import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@Slf4j
public class CookieProvider {
   // @Value("${MODE}")
 //   private String mode;

    public Cookie generateCookie(String key, String value, int time) {
        Cookie cookie = new Cookie(key, value);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setSecure(true);
        cookie.setMaxAge(time);
        return cookie;
    }
}
