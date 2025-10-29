package seoil.capstone.flashbid.domain.auth.service;

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
import seoil.capstone.flashbid.domain.auth.entity.UserFcmEntity;
import seoil.capstone.flashbid.domain.auth.repository.EmailOtpRedisRepository;
import seoil.capstone.flashbid.domain.auth.repository.FcmCacheRepository;
import seoil.capstone.flashbid.domain.auth.repository.UserFcmRepository;
import seoil.capstone.flashbid.domain.user.entity.Account;
import seoil.capstone.flashbid.domain.user.repository.AccountRepository;
import seoil.capstone.flashbid.global.common.enums.LoginType;
import seoil.capstone.flashbid.global.common.enums.UserStatus;
import seoil.capstone.flashbid.global.common.enums.UserType;
import seoil.capstone.flashbid.global.common.error.ApiException;
import seoil.capstone.flashbid.global.core.provider.JwtProvider;
import seoil.capstone.flashbid.infrastructure.mail.EmailTemplate;
import seoil.capstone.flashbid.infrastructure.mail.MailService;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final JwtProvider jwtProvider;
    private final AccountRepository accountRepository;
    private final EmailOtpRedisRepository emailOtpRedisRepository;
    private final EmailTemplate emailTemplate;
    private final MailService mailService;
    private final UserFcmRepository userFcmRepository;
    private final FcmCacheRepository fcmCacheRepository;

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

    public void authorizeOtpCode(String email) {
        String otpCode = emailOtpRedisRepository.generateOtp(email);
        mailService.sendHtmlMailAsync(
                email,
                "[Unknown Auction] 이메일 인증 코드",
                emailTemplate.getVerificationEmailTemplate(otpCode)
        );
    }

    public boolean verifyOtpCode(String email, String otp) {
        if(!emailOtpRedisRepository.validateOtp(email, otp)){
            throw new ApiException(HttpStatus.BAD_REQUEST, "인증코드 인증 실패", "인증 코드가 올바르지 않거나 만료되었습니다.");
        }
        return true;
    }

    public String saveFcmToken(Long userId, String fcmToken) {
        String cacheFcmToken = fcmCacheRepository.getFcmToken(userId);

        // 1. 캐시에 토큰 존재하고 같으면 그대로 리턴
        if (cacheFcmToken != null && cacheFcmToken.equals(fcmToken)) {
            return cacheFcmToken;
        }

        // 2. DB에서 토큰 조회
        Optional<UserFcmEntity> users = userFcmRepository.findByAccountId(userId);
        if (users.isPresent()) {
            UserFcmEntity userFcmEntity = users.get();
            if (!userFcmEntity.getToken().equals(fcmToken)) {
                userFcmEntity.setToken(fcmToken);
                userFcmRepository.save(userFcmEntity);
                fcmCacheRepository.saveFcmToken(userId, fcmToken);
            }
        } else {
            // 2-2. DB에 토큰이 존재하지 않을 경우 -> 생성
            userFcmRepository.save(UserFcmEntity.builder()
                    .account(accountRepository.findById(userId).orElseThrow())
                    .token(fcmToken)
                    .build());
            fcmCacheRepository.saveFcmToken(userId, fcmToken);
        }
        return fcmToken;
    }
}
