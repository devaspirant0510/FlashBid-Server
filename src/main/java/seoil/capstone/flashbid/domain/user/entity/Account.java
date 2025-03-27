package seoil.capstone.flashbid.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;
import seoil.capstone.flashbid.global.enums.LoginType;
import seoil.capstone.flashbid.global.enums.UserStatus;
import seoil.capstone.flashbid.global.enums.UserType;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Builder
@AllArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated
    private LoginType loginType;

    @Enumerated
    private UserStatus userStatus;

    @Enumerated
    private UserType userType;

    @Column
    private String email;

    @Column
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime deletedAt;

    @Column
    private boolean isVerified;

    @Column
    private String uuid;

    public Account(LoginType loginType, UserStatus userStatus, UserType userType, String email, LocalDateTime createdAt, LocalDateTime deletedAt, boolean isVerified, String uuid) {
        this.loginType = loginType;
        this.userStatus = userStatus;
        this.userType = userType;
        this.email = email;
        this.createdAt = createdAt;
        this.deletedAt = deletedAt;
        this.isVerified = isVerified;
        this.uuid = uuid;
    }
}
