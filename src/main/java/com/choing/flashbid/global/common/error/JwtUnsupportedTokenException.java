package com.choing.flashbid.global.common.error;

import com.choing.flashbid.global.common.response.ErrorDetails;

public class JwtUnsupportedTokenException extends ApiException {
    public JwtUnsupportedTokenException() {
        super(new ErrorDetails(
                null,
                "Unauthorized",
                401,
                "지원하지 않는 JWT 토큰입니다.",
                null,
                "E401_JWT_UNSUPPORTED"
        ));
    }
}
