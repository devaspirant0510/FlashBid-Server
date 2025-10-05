package seoil.capstone.flashbid.global.core.security;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import seoil.capstone.flashbid.global.common.response.ErrorDetails;
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
        response.setContentType("application/json;charset=UTF-8");
        ErrorDetails errorDetails = new ErrorDetails(
                null,
                "Unauthorized",
                401,
                "인증되지 않은 사용자입니다. 로그인후 이용해주세요.",
                request.getRequestURI()
        );
        ApiResult<?> unauthorized =
                ApiResult.builder()
                        .status(401)
                        .message("Unauthorized")
                        .error(errorDetails)
                        .success(false)
                        .timestamp(LocalDateTime.now())
                        .build();
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        response.getWriter().write(objectMapper.writeValueAsString(unauthorized));
    }
}
