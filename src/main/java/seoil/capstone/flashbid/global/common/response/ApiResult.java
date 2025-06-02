package seoil.capstone.flashbid.global.common.response;


import jakarta.servlet.http.HttpServletRequest;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiResult<T> {
    private ApiHeader apiHeader;
    private String path;
    private String method;
    private LocalDateTime timestamp;
    private T data;
    private ApiError error;
    private String message;

    public static <T> ApiResult<T> ok(T data, HttpServletRequest request) {
        return ApiResult.<T>builder()
                .apiHeader(new ApiHeader(
                        HttpStatus.OK
                ))
                .method(request.getMethod())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(data)
                .build();

    }

    public static <T> ApiResult<T> created(T data,HttpServletRequest request){
        return ApiResult.<T>builder()
                .apiHeader(new ApiHeader(
                        HttpStatus.CREATED
                ))
                .method(request.getMethod())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(data)
                .build();
    }

    public static <T> ApiResult<T> error(String errorCode,String errorMessage,HttpServletRequest request,HttpStatus status){
        return ApiResult.<T>builder()
                .apiHeader(new ApiHeader(
                        HttpStatus.CREATED
                ))
                .method(request.getMethod())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .data(null)
                .error(
                        new ApiError(
                                errorCode,errorMessage
                        )
                )
                .build();

    }
}
