package seoil.capstone.flashbid.global.common.response;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiResult<T> {
    private T data;
    private String message;
    private Boolean success;
    private ErrorDetails error;
    private LocalDateTime timestamp;

    @JsonIgnore
    private int status;

    public static <T> ApiResult<T> ok(T data) {
        return new ApiResult<T>()
                .status(200)
                .data(data)
                .message("성공")
                .success(true)
                .timestampNow();
    }

    public static <T> ApiResult<T> ok(T data, String message) {
        return new ApiResult<T>()
                .status(200)
                .data(data)
                .message(message)
                .success(true)
                .timestampNow();
    }

    public static <T> ApiResult<T> created(T data) {
        return new ApiResult<T>()
                .status(201)
                .data(data)
                .message("생성됨")
                .success(true)
                .timestampNow();
    }

    public static <T> ApiResult<T> created(T data, String message) {
        return new ApiResult<T>()
                .status(201)
                .data(data)
                .message(message)
                .success(true)
                .timestampNow();
    }

    public static <T> ApiResult<T> error(HttpStatus status, String title, String content) {
        return new ApiResult<T>()
                .status(status.value())
                .message("서버 오류")
                .success(false)
                .data(null)
                .error(new ErrorDetails(null, title, status.value(), content, null))
                .timestampNow();
    }

    public ApiResult<T> status(int status) {
        this.status = status;
        return this;
    }

    public ApiResult<T> message(String message) {
        this.message = message;
        return this;
    }

    public ApiResult<T> data(T data) {
        this.data = data;
        this.timestamp = LocalDateTime.now();
        return this;
    }

    public ApiResult<T> success(boolean success) {
        this.success = success;
        return this;
    }

    public ApiResult<T> error(ErrorDetails error) {
        this.error = error;
        return this;
    }

    private ApiResult<T> timestampNow() {
        this.timestamp = LocalDateTime.now();
        return this;
    }
}
