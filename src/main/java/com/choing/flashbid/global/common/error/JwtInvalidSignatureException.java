package com.choing.flashbid.global.common.error;

import com.choing.flashbid.global.common.response.ErrorDetails;

public class JwtInvalidSignatureException extends ApiException {
    public JwtInvalidSignatureException() {
        super(new ErrorDetails(
                null,
                "Unauthorized",
                401,
                "JWT 서명이 올바르지 않습니다.",
                null,
                "E401_JWT_INVALID_SIGNATURE"
        ));
    }
}

