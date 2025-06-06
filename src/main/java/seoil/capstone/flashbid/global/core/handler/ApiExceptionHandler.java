package seoil.capstone.flashbid.global.core.handler;


import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import seoil.capstone.flashbid.global.common.error.ApiException;
import seoil.capstone.flashbid.global.common.response.ApiError;
import seoil.capstone.flashbid.global.common.response.ApiHeader;
import seoil.capstone.flashbid.global.common.response.ApiResult;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {


    @ExceptionHandler(Exception.class)
    public <T> ResponseEntity<ApiResult<T>> exceptionHandler(Exception e, HttpServletRequest request) {
        log.error(e.toString());
        e.printStackTrace();
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

    @ExceptionHandler(ApiException.class)
    public <T> ResponseEntity<ApiResult<T>> apiExceptionHandler(ApiException e,HttpServletRequest request){

        log.error(e.toString());
        e.printStackTrace();
        ApiResult<T> result = ApiResult.<T>builder()
                .apiHeader(
                        new ApiHeader(
                                e.getStatus()
                        )
                )
                .error(
                        e.getApiError()
                )
                .data(null)
                .path(request.getRequestURI())
                .method(request.getMethod())
                .timestamp(LocalDateTime.now())
                .message(e.getMessage())
                .build();
        return ResponseEntity.status(e.getStatus()).body(result);
    }
}
