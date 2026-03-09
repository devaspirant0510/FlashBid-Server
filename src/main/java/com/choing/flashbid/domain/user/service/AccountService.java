package com.choing.flashbid.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.choing.flashbid.domain.file.entity.FileEntity;
import com.choing.flashbid.domain.file.service.FileService;
import com.choing.flashbid.domain.payment.entity.PointHistoryEntity;
import com.choing.flashbid.domain.payment.repository.PointHistoryRepository;
import com.choing.flashbid.domain.user.entity.Account;
import com.choing.flashbid.domain.user.repository.AccountRepository;
import com.choing.flashbid.global.common.enums.FileType;
import com.choing.flashbid.global.common.enums.LoginType;
import com.choing.flashbid.global.common.enums.UserStatus;
import com.choing.flashbid.global.common.enums.UserType;
import com.choing.flashbid.infrastructure.id.SnowflakeGenerator;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final FileService fileService;

    private final AccountRepository accountRepository;
    private final PointHistoryRepository pointHistoryRepository;
    private final SnowflakeGenerator snowflakeGenerator;

    // 이메일로 가입한 유저의 이메일을 디비에서 조회하여 가입한적이 있는지 확인
    public boolean isRegisteredEmail(String email) {
        return accountRepository.existsByEmail(email);
    }
    // 닉네임으로 가입한 유저의 닉네임을 디비에서 조회하여 가입한적이 있는지 확인
    public boolean isRegisteredNickname(String nickname) {
        return accountRepository.existsByNickname(nickname);
    }

    // OAuth 로 로그인된 유저의 UUID 를 디비에서 조회하여 가입한적이 있는지 확인
    public boolean isRegisteredUser(String uuid) {
        Optional<Account> user = accountRepository.findByUuid(uuid);
        return user.isPresent();
    }

    public Account getUserByUuid(String uuid) {
        return accountRepository.findByUuid(uuid).orElse(null);
    }

    // OAuth 로그인 성공시 필수 정보 기반으로 Account 테이블 생성
    @Transactional
    public Account registerAccount(String email, String uuid, LoginType loginType) {
        Account createAccount = Account.builder()
                .id(snowflakeGenerator.nextId())
                .email(email)
                .uuid(uuid)
                .isVerified(false)
                .loginType(loginType)
                .userType(UserType.UN_REGISTER)
                .userStatus(UserStatus.UN_LINK)
                .build();
        createAccount.setPoint(1000000);
        Account save = accountRepository.save(createAccount);
        PointHistoryEntity pointHistory = PointHistoryEntity.builder()
                .chargeType(PointHistoryEntity.ChargeType.GIFT)
                .contents("가입 축하 포인트 지급")
                .earnedPoint(1000000)
                .earnType(PointHistoryEntity.EarnType.EARN)
                .userId(createAccount)
                .build();
        pointHistoryRepository.save(pointHistory);

        return save;
    }

    public void updateUserProfile(Long userId, String newNickname, MultipartFile profileImage) throws IOException {
        Account user = accountRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다. id=" + userId));

        if (newNickname != null && !newNickname.isEmpty()) {
            user.setNickname(newNickname);
        }

        if (profileImage != null && !profileImage.isEmpty()) {
            List<FileEntity> savedImageInfo = fileService.uploadAllFiles(Collections.singletonList(profileImage),user,user.getId(), FileType.PROFILE);
            if (!savedImageInfo.isEmpty()) {
                String imageUrl = savedImageInfo.get(0).getUrl();
                user.setProfileUrl(imageUrl);
            }
        }
        accountRepository.save(user);
    }



}
