package seoil.capstone.flashbid.global.common.error;

import seoil.capstone.flashbid.global.common.response.ErrorDetails;

/**
 * 단일 토큰 인증 예외 클래스.
 * title/detail/errorCode 를 직접 지정해서 사용합니다.
 */
public class TokenUnAuthorized extends ApiException {

    // 기본 단일 생성자 (요구사항: 생성자 파라미터로 직접 받기)
    public TokenUnAuthorized(String title, String detail, String errorCode) {
        super(new ErrorDetails(null, title, 401, detail, null, errorCode));
    }
}
