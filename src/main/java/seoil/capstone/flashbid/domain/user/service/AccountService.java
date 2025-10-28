package seoil.capstone.flashbid.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import seoil.capstone.flashbid.domain.file.dto.SaveFileDto;
import seoil.capstone.flashbid.domain.file.entity.FileEntity;
import seoil.capstone.flashbid.domain.file.service.FileService;
import seoil.capstone.flashbid.domain.user.entity.Account;
import seoil.capstone.flashbid.domain.user.repository.AccountRepository;
import seoil.capstone.flashbid.global.common.enums.FileType;
import seoil.capstone.flashbid.global.common.enums.LoginType;
import seoil.capstone.flashbid.global.common.enums.UserStatus;
import seoil.capstone.flashbid.global.common.enums.UserType;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final FileService fileService;

    private final AccountRepository accountRepository;
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
        return accountRepository.findByUuid(uuid).orElseThrow(() -> {
            throw new IllegalStateException("유저정보를 찾는데 실패헀습니다.");
        });
    }

    // OAuth 로그인 성공시 필수 정보 기반으로 Account 테이블 생성
    public Account registerAccount(String email, String uuid, LoginType loginType) {
        Account createAccount = Account.builder()
                .email(email)
                .uuid(uuid)
                .isVerified(false)
                .loginType(loginType)
                .userType(UserType.UN_REGISTER)
                .userStatus(UserStatus.UN_LINK)
                .build();
        return accountRepository.save(createAccount);
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
