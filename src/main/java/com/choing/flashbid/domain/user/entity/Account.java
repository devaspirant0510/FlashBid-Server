package com.choing.flashbid.domain.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.*;
import com.choing.flashbid.global.common.enums.LoginType;
import com.choing.flashbid.global.common.enums.UserStatus;
import com.choing.flashbid.global.common.enums.UserType;
import com.choing.flashbid.global.core.BaseTimeEntity;

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
    private Long id;

    @Enumerated
    private LoginType loginType;

    @Enumerated
    private UserStatus userStatus;

    @Enumerated
    private UserType userType;

    @Column
    String password;

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

    @Column
    private String profileUrl;

    @Column(nullable = false, columnDefinition = "integer default 0")
    @Builder.Default
    private Integer point = 0;

    public Account(LoginType loginType, UserStatus userStatus, UserType userType, String email, String password, LocalDateTime deletedAt, boolean isVerified, String uuid) {
        this.loginType = loginType;
        this.userStatus = userStatus;
        this.userType = userType;
        this.email = email;
        this.password = password;
        this.deletedAt = deletedAt;
        this.isVerified = isVerified;
        this.uuid = uuid;
    }
}
