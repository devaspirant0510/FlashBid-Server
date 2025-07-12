package seoil.capstone.flashbid.global.core.provider;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Component
@RequiredArgsConstructor
@Slf4j
public class CookieProvider {
    public Cookie generateToken(String key,String value){
        Cookie cookie = new Cookie(key,value);
        return cookie;

    }
}
