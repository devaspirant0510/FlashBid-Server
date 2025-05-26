package seoil.capstone.flashbid.global.core.handler;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import seoil.capstone.flashbid.global.common.response.ApiError;
import seoil.capstone.flashbid.global.common.response.ApiHeader;
import seoil.capstone.flashbid.global.common.response.ApiResult;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(Exception.class)
    public <T> ResponseEntity<ApiResult<T>> exceptionHandler(Exception e, HttpServletRequest request) {
        ApiResult<T> result = ApiResult.<T>builder()
                .apiHeader(
                        new ApiHeader(
                                HttpStatus.INTERNAL_SERVER_ERROR
                        )
                )
                .error(
                        new ApiError("500E00", "예기치 못한 오류 발생")
                )
                .data(null)
                .path(request.getRequestURI())
                .method(request.getMethod())
                .timestamp(LocalDateTime.now())
                .message(e.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);

    }
}
