package seoil.capstone.flashbid.global.common.error;


public class LoginFailedPasswordOrEmailIncorrect extends Exception {
    public LoginFailedPasswordOrEmailIncorrect() {
        super("로그인을 실패 했습니다. 이메일 또는 비밀번호가 일치 하지 않습니다.");
    }
}
