package seoil.capstone.flashbid.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import seoil.capstone.flashbid.domain.user.entity.Account;
import seoil.capstone.flashbid.domain.user.repository.AccountRepository;
import seoil.capstone.flashbid.global.enums.LoginType;
import seoil.capstone.flashbid.global.enums.UserStatus;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

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
                .createdAt(LocalDateTime.now())
                .isVerified(false)
                .loginType(loginType)
                .userStatus(UserStatus.UN_LINK)
                .build();
        return accountRepository.save(createAccount);
    }

}
