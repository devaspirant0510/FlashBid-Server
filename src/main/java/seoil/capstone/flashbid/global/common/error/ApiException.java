package seoil.capstone.flashbid.global.common.error;


import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import seoil.capstone.flashbid.global.common.response.ErrorDetails;

@Getter
@Setter
public class ApiException extends RuntimeException {
    private final ErrorDetails error;

    public ApiException(ErrorDetails error) {
        super(error.getDetail());
        this.error = error;
    }

    public ApiException(HttpStatus status, String title, String detail) {
        this.error = new ErrorDetails(null, title, status.value(), detail, null);
    }

    public ApiException(HttpStatus status, String title, String detail, String errorCode) {
        this.error = new ErrorDetails(null, title, status.value(), detail, null, errorCode);
    }

    public ApiException(int status, String title, String detail) {
        this.error = new ErrorDetails(null, title, status, detail, null);
    }

    public ApiException(int status, String title, String detail, String errorCode) {
        this.error = new ErrorDetails(null, title, status, detail, null, errorCode);
    }


    public ApiException(int statusCode) {
        this.error = new ErrorDetails(
                null,
                HttpStatus.valueOf(statusCode).name(),
                statusCode,
                HttpStatus.valueOf(statusCode).getReasonPhrase(),
                null
        );
    }

    public ApiException(HttpStatus status) {
        this.error = new ErrorDetails(
                null,
                status.name(),
                status.value(),
                status.getReasonPhrase(),
                null
        );
    }
}
