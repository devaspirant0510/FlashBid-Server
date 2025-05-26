package seoil.capstone.flashbid.global.core.module.security;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import seoil.capstone.flashbid.global.common.response.ApiError;
import seoil.capstone.flashbid.global.common.response.ApiHeader;
import seoil.capstone.flashbid.global.common.response.ApiResult;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    private final ObjectMapper objectMapper;
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ApiResult<Object> result = ApiResult.builder()
                .apiHeader(new ApiHeader(HttpStatus.FORBIDDEN))
                .error(new ApiError("403E00", "접근 권한이 없습니다"))
                .path(request.getRequestURI())
                .message(accessDeniedException.getMessage())
                .timestamp(LocalDateTime.now())
                .data(null)
                .build();
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }
}
