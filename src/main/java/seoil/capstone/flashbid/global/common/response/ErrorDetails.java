package seoil.capstone.flashbid.global.common.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ErrorDetails {
    // error 에 관련된 문서 링크
    private String type;
    // error 제목
    private String title;
    // HTTP 상태 코드
    private Integer status;
    // error 상세 내용
    private String detail;
    // error 가 발생한 instance URI
    private String instance;
    // custom error code
    private String errorCode;

    public ErrorDetails(String type, String title, Integer status, String detail, String instance) {
        this.type = type;
        this.title = title;
        this.status = status;
        this.detail = detail;
        this.instance = instance;
    }
}
