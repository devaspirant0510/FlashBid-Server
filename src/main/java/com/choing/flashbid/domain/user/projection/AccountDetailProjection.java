package com.choing.flashbid.domain.user.projection;


import com.choing.flashbid.global.common.enums.LoginType;
import com.choing.flashbid.global.common.enums.UserStatus;
import com.choing.flashbid.global.common.enums.UserType;

import java.time.LocalDateTime;

public interface AccountDetailProjection {
    Long getId();
    String getEmail();
    String getNickname();

    UserStatus getUserStatus();
    UserType getUserType();
    LoginType getLoginType();
    LocalDateTime getDeletedAt();
    boolean isVerified();
    String getUuid();
    String getDescription();
    String getProfileUrl();
    Integer getPoint();
}
