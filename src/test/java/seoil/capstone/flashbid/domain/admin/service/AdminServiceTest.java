package seoil.capstone.flashbid.domain.admin.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import seoil.capstone.flashbid.domain.admin.repository.jpa.AccountAdminRepository;
import seoil.capstone.flashbid.domain.user.projection.AccountDetailProjection;
import seoil.capstone.flashbid.global.common.enums.LoginType;
import seoil.capstone.flashbid.global.common.enums.UserStatus;
import seoil.capstone.flashbid.global.common.enums.UserType;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
class AdminServiceTest {
    @Mock
    private AccountAdminRepository accountAdminRepository;

    @InjectMocks
    private AdminService adminService;

    @Test
    @DisplayName("관리자 페이지 유저 리스트 조회")
    void searchUserList() {
        // given
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);
        AccountDetailProjection a1 =
                new FakeAccount(
                        1L,
                        "s",
                        "s",
                        UserStatus.UN_LINK,
                        UserType.CUSTOMER,
                        LoginType.NAVER,
                        null,
                        true,
                        "uuid-1",
                        "desc",
                        "profile",
                        0
                );

        AccountDetailProjection a2 =
                new FakeAccount(
                        2L,
                        "s2",
                        "s2",
                        UserStatus.UN_LINK,
                        UserType.CUSTOMER,
                        LoginType.APPLE,
                        null,
                        true,
                        "uuid-2",
                        "desc",
                        "profile",
                        0
                );
        Page<AccountDetailProjection> mockPage = new PageImpl<>(List.of(a1, a2), pageable, 2);
        given(
                accountAdminRepository.findAllBy(pageable, AccountDetailProjection.class)
        ).willReturn(mockPage);

        System.out.println(mockPage);
        // when
//        Page<AccountDetailProjection> auctionUsers = adminService.getAuctionUsers(page, size, UserStatus.ACTIVE);
//
//
//        // then
//        assertThat(auctionUsers.getTotalElements()).isEqualTo(2);
    }

}
class FakeAccount implements AccountDetailProjection {

    private final Long id;
    private final String email;
    private final String nickname;
    private final UserStatus userStatus;
    private final UserType userType;
    private final LoginType loginType;
    private final LocalDateTime deletedAt;
    private final boolean verified;
    private final String uuid;
    private final String description;
    private final String profileUrl;
    private final Integer point;

    FakeAccount(
            Long id,
            String email,
            String nickname,
            UserStatus userStatus,
            UserType userType,
            LoginType loginType,
            LocalDateTime deletedAt,
            boolean verified,
            String uuid,
            String description,
            String profileUrl,
            Integer point
    ) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.userStatus = userStatus;
        this.userType = userType;
        this.loginType = loginType;
        this.deletedAt = deletedAt;
        this.verified = verified;
        this.uuid = uuid;
        this.description = description;
        this.profileUrl = profileUrl;
        this.point = point;
    }

    @Override public Long getId() { return id; }
    @Override public String getEmail() { return email; }
    @Override public String getNickname() { return nickname; }
    @Override public UserStatus getUserStatus() { return userStatus; }
    @Override public UserType getUserType() { return userType; }
    @Override public LoginType getLoginType() { return loginType; }
    @Override public LocalDateTime getDeletedAt() { return deletedAt; }
    @Override public boolean isVerified() { return verified; }
    @Override public String getUuid() { return uuid; }
    @Override public String getDescription() { return description; }
    @Override public String getProfileUrl() { return profileUrl; }
    @Override public Integer getPoint() { return point; }
}
