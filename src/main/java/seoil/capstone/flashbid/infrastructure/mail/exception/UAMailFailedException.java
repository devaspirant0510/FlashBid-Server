package seoil.capstone.flashbid.infrastructure.mail.exception;


import org.springframework.http.HttpStatus;
import seoil.capstone.flashbid.global.common.error.ApiException;

public class UAMailFailedException extends ApiException {
    public UAMailFailedException(){
        super(HttpStatus.INTERNAL_SERVER_ERROR,"메일 전송 실패","시스템 오류로 인한 메일전송에 실패했습니다. 잠시 후 다시 시도해주세요.","E500_SYSTEM_MAIL001_MAIL_SEND_FAILED");
    }
}
