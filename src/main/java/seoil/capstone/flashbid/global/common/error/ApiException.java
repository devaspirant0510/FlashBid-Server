package seoil.capstone.flashbid.global.common.error;


import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import seoil.capstone.flashbid.global.common.response.ApiError;

@Getter
@Setter
public class ApiException extends RuntimeException {
    private final ApiError apiError;
    private final HttpStatus status;

    public ApiException(HttpStatus status, ApiError apiError) {
        super(apiError.getErrorMessage());
        this.apiError = apiError;
        this.status = status;
    }

    public ApiException(HttpStatus status, String errorCode, String errorMessage) {
        super(errorMessage);
        this.status = status;
        this.apiError = new ApiError(errorCode, errorMessage);
    }
}
