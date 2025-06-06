package seoil.capstone.flashbid.global.common.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ApiHeader {
    private int code;
    private String status;
    public ApiHeader(HttpStatus status){
        this.code = status.value();
        this.status = status.getReasonPhrase();

    }
}
