package seoil.capstone.flashbid.domain.user.projection;


import seoil.capstone.flashbid.global.common.enums.LoginType;
import seoil.capstone.flashbid.global.common.enums.UserStatus;
import seoil.capstone.flashbid.global.common.enums.UserType;

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
