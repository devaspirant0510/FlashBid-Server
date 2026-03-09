package com.choing.flashbid.global.core.provider;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@Slf4j
public class ClientIdentifierProvider {
    public String extractClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");

        if (xForwardedFor != null && !xForwardedFor.isBlank()) {
            // 프록시 여러 개면 첫 번째가 원본 IP
            return xForwardedFor.split(",")[0].trim();
        }

        return request.getRemoteAddr();
    }

    public String extractUserAgent(HttpServletRequest request) {
        return request.getHeader("User-Agent");
    }
}
