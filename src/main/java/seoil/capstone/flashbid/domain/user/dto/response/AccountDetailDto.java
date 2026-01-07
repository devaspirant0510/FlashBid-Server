package seoil.capstone.flashbid.domain.user.dto.response;

import lombok.*;
import seoil.capstone.flashbid.domain.user.projection.AccountDetailProjection;
import seoil.capstone.flashbid.global.common.enums.LoginType;
import seoil.capstone.flashbid.global.common.enums.UserStatus;
import seoil.capstone.flashbid.global.common.enums.UserType;

import java.time.LocalDateTime;


@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AccountDetailDto implements AccountDetailProjection {
    private Long id;
    private String email;
    private String nickname;
    private UserStatus userStatus;
    private UserType userType;
    private LoginType loginType;
    private LocalDateTime deletedAt;
    private boolean verified;
    private String uuid;
    private String description;
    private String profileUrl;
    private Integer point;
}
