package com.choing.flashbid.global.common.error;

import com.choing.flashbid.global.common.response.ErrorDetails;

public class JwtExpiredTokenException extends ApiException {
    public JwtExpiredTokenException() {
        super(new ErrorDetails(
                null,
                "Unauthorized",
                401,
                "JWT 토큰이 만료되었습니다.",
                null,
                "E401_JWT_EXPIRED"
        ));
    }
}
