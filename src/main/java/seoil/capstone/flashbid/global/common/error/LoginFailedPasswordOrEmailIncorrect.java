package seoil.capstone.flashbid.global.common.error;


import seoil.capstone.flashbid.global.common.response.ErrorDetails;

public class LoginFailedPasswordOrEmailIncorrect extends ApiException {
    public LoginFailedPasswordOrEmailIncorrect() {
        super(new ErrorDetails(
                "https://chlorinated-peripheral-27a.notion.site/E401_AUTH_INCORRECT_ID_AND_PASSWORD-2832f64f80ba80209546f9b5bde5dbe1",
                "Login Failed",
                401,
                "이메일 또는 비밀번호가 올바르지 않습니다.",
                null,
                "E401_AUTH_INCORRECT_ID_AND_PASSWORD"
        ));
    }
}
