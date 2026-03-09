package com.choing.flashbid.global.common.error;

import com.choing.flashbid.global.common.response.ErrorDetails;

public class JwtMalformedTokenException extends ApiException {
    public JwtMalformedTokenException() {
        super(new ErrorDetails(
                null,
                "Unauthorized",
                401,
                "JWT 형식이 올바르지 않습니다.",
                null,
                "E401_JWT_MALFORMED"
        ));
    }
}
