package com.choing.flashbid.global.common.error;

import com.choing.flashbid.global.common.response.ErrorDetails;

public class JwtEmptyClaimsException extends ApiException {
    public JwtEmptyClaimsException() {
        super(new ErrorDetails(
                null,
                "Unauthorized",
                401,
                "JWT 클레임이 비어있습니다.",
                null,
                "E401_JWT_EMPTY_CLAIMS"
        ));
    }
}
