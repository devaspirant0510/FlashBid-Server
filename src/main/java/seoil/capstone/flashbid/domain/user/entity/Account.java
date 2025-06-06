package seoil.capstone.flashbid.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;
import seoil.capstone.flashbid.global.common.enums.LoginType;
import seoil.capstone.flashbid.global.common.enums.UserStatus;
import seoil.capstone.flashbid.global.common.enums.UserType;
import seoil.capstone.flashbid.global.core.BaseTimeEntity;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Builder
@AllArgsConstructor
public class Account extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated
    private LoginType loginType;

    @Enumerated
    private UserStatus userStatus;

    @Enumerated
    private UserType userType;

    @Column(nullable = false)
    private String email;

    @Column
    private LocalDateTime deletedAt;

    @Column(nullable = false)
    private boolean isVerified;

    @Column(nullable = false)
    private String uuid;

    @Column
    private String nickname;

    @Column
    private String description;

    public Account(LoginType loginType, UserStatus userStatus, UserType userType, String email,  LocalDateTime deletedAt, boolean isVerified, String uuid) {
        this.loginType = loginType;
        this.userStatus = userStatus;
        this.userType = userType;
        this.email = email;
        this.deletedAt = deletedAt;
        this.isVerified = isVerified;
        this.uuid = uuid;
    }
}
