package seoil.capstone.flashbid.domain.auth.service;

import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import seoil.capstone.flashbid.domain.auth.dto.AuthTokenDto;
import seoil.capstone.flashbid.domain.auth.dto.RegisterDto;
import seoil.capstone.flashbid.domain.auth.dto.RegisterEmailDto;
import seoil.capstone.flashbid.domain.user.entity.Account;
import seoil.capstone.flashbid.domain.user.repository.AccountRepository;
import seoil.capstone.flashbid.global.common.enums.LoginType;
import seoil.capstone.flashbid.global.common.enums.UserStatus;
import seoil.capstone.flashbid.global.common.enums.UserType;
import seoil.capstone.flashbid.global.common.error.ApiException;
import seoil.capstone.flashbid.global.core.provider.JwtProvider;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final JwtProvider jwtProvider;
    private final AccountRepository accountRepository;

    public Object makeCookie(String accessToken, String refreshToken) {
        Cookie accessCookie = new Cookie("access_token", accessToken);

        Cookie refreshCookie = new Cookie("refresh_token", refreshToken);

        return null;

    public Account authorizationTokenWithUser(String token) {
        log.info(token);
        if (!jwtProvider.validateToken(token)) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "토큰 인증 실패", "");
        }
        String uuid = jwtProvider.parseClaims(token).getSubject();
        return accountRepository.findByUuid(uuid).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "", ""));
    }

    public AuthTokenDto createJwtToken(Account account) {
        String accessToken = jwtProvider.createAccessToken(account.getUuid(), account);
        String refreshToken = jwtProvider.createRefreshToken(account.getUuid());
        return new AuthTokenDto(
                accessToken,
                refreshToken
        );
    }

    @Transactional
    public Account registerUser(RegisterDto dto) {
        Account account = accountRepository.findByUuid(dto.getUid()).orElseThrow(() ->
                new ApiException(HttpStatus.NOT_FOUND, "", ""));
        account.setNickname(dto.getNickname());
        account.setUserType(UserType.CUSTOMER);
        return account;
    }

    @Transactional
    public Account registerUserWithEmail(RegisterEmailDto dto) {
        if (accountRepository.existsByEmail(dto.getEmail())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "400E00A22", "이미 가입된 이메일 입니다.");
        }
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodePassword = passwordEncoder.encode(dto.getPassword());
        System.out.println(encodePassword);

        Account createAccount = Account.builder()
                .userType(UserType.CUSTOMER)
                .userStatus(UserStatus.ACTIVE)
                .uuid(UUID.randomUUID().toString())
                .loginType(LoginType.EMAIL)
                .isVerified(false)
                .nickname(dto.getNickname())
                .password(encodePassword)
                .email(dto.getEmail())
                .build();
        accountRepository.save(createAccount);
        return createAccount;
    }


    public void reGenRefreshToken() {

    }
}
