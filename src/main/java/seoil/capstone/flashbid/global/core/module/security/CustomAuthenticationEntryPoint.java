package seoil.capstone.flashbid.global.core.module.security;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import seoil.capstone.flashbid.global.common.response.ApiError;
import seoil.capstone.flashbid.global.common.response.ApiHeader;
import seoil.capstone.flashbid.global.common.response.ApiResult;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        ApiResult<Object> result = ApiResult.builder()
                .apiHeader(new ApiHeader(HttpStatus.UNAUTHORIZED))
                .error(new ApiError("401E00", "인증이 필요합니다"))
                .path(request.getRequestURI())
                .message(authException.getMessage())
                .timestamp(LocalDateTime.now())
                .data(null)
                .build();

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }
}
