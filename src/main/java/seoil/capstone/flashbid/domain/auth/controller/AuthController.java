package seoil.capstone.flashbid.domain.auth.controller;
import org.springframework.http.ResponseCookie;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import seoil.capstone.flashbid.domain.auth.dto.AuthTokenDto;
import seoil.capstone.flashbid.domain.auth.dto.EmailAuthLoginDto;
import seoil.capstone.flashbid.domain.auth.dto.RegisterDto;
import seoil.capstone.flashbid.domain.auth.dto.RegisterEmailDto;
import seoil.capstone.flashbid.domain.auth.service.AuthService;
import seoil.capstone.flashbid.domain.user.entity.Account;
import seoil.capstone.flashbid.domain.user.repository.AccountRepository;
import seoil.capstone.flashbid.domain.user.service.AccountService;
import seoil.capstone.flashbid.global.common.AuthRestClient;
import seoil.capstone.flashbid.global.common.enums.LoginType;
import seoil.capstone.flashbid.global.common.error.TokenUnAuthorized;
import seoil.capstone.flashbid.global.common.response.ApiResult;
import seoil.capstone.flashbid.global.core.provider.CookieProvider;
import seoil.capstone.flashbid.global.core.provider.JwtProvider;
import seoil.capstone.flashbid.global.model.*;

import java.io.IOException;


@Slf4j
@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthRestClient restClient;
    private final AccountService accountService;
    private final JwtProvider jwtProvider;
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final AccountRepository accountRepository;
    private final CookieProvider cookieProvider;

    // 가입된 이메일이 있는지 확인
    @GetMapping("/register/email/check")
    public ApiResult<Boolean> checkEmail(@RequestParam("email") String email, HttpServletRequest request) {
        boolean isRegistered = accountService.isRegisteredEmail(email);
        return ApiResult.ok(isRegistered);
    }
    // 닉네임 중복 확인
    @GetMapping("/register/nickname/check")
    public ApiResult<Boolean> checkNickname(@RequestParam("nickname") String nickname, HttpServletRequest request) {
        boolean isRegistered = accountService.isRegisteredNickname(nickname);
        return ApiResult.ok(isRegistered);
    }
    @PostMapping("/token")
    public ApiResult<String> reissueToken(@CookieValue(value = "refresh_token", required = false) String refreshToken, HttpServletRequest request, HttpServletResponse response) {
        if (refreshToken == null || !jwtProvider.validateToken(refreshToken)) {
            throw new TokenUnAuthorized("토큰 인증 실패", "리프레시 토큰이 유효하지 않습니다.", "TOKEN_UNAUTHORIZED");
        }
        Claims claims = jwtProvider.parseClaims(refreshToken);
        String uid = claims.getSubject();
        Account account = accountService.getUserByUuid(uid);
        AuthTokenDto token = authService.createAccessToken(account);

        String reGenRefreshToken = jwtProvider.createRefreshToken(uid, claims.getExpiration().getTime());
        // rtr 리프레시 토큰 업데이트
        response.addCookie(
                cookieProvider.generateCookie("refresh_token", reGenRefreshToken, 60 * 60 * 24)
        );
        return ApiResult.ok(token.getAccessToken());
    }

    @PostMapping("/login")
    public ApiResult<Account> login(@RequestBody EmailAuthLoginDto dto, HttpServletRequest request, HttpServletResponse response) {
        //  이메일+패스워드 인증

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
        );

        //  인증 성공 → SecurityContext 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info(authentication.getName());
        Account account = accountRepository.findByEmail(authentication.getName()).orElseThrow();
        String accessToken = jwtProvider.createAccessToken(account.getUuid(), account);


//        // 쿠키에 저장
//        Cookie refreshCookie = new Cookie("refresh_token", token.getRefreshToken());
//        refreshCookie.setHttpOnly(false);
//        refreshCookie.setSecure(false);
//        refreshCookie.setMaxAge(60 * 60 * 24);
//        refreshCookie.setPath("/");
//        response.addCookie(refreshCookie);
//
        Cookie accessCookie = new Cookie("accessToken", accessToken);
        accessCookie.setHttpOnly(false);
        accessCookie.setSecure(false);
        accessCookie.setMaxAge(60 * 60 * 24);
        accessCookie.setPath("/");
        response.addCookie(accessCookie);

        return ApiResult.ok(account);
    }

    @PostMapping("/register/oauth")
    public ApiResult<Account> registerService(@RequestBody RegisterDto dto,HttpServletRequest request){
        //TODO : 가입 여부 확인
        return ApiResult.ok(authService.registerUser(dto));
    }
    @PostMapping("/register/email")
    public ApiResult<Account> registerEmail(@RequestBody RegisterEmailDto dto, HttpServletRequest request){
        return ApiResult.ok(authService.registerUserWithEmail(dto));

    }

    @GetMapping("/callback/naver")
    public ApiResult<Account> naverAuthCallback(
            @RequestParam("code") String code,
            @RequestParam("redirect") String redirect,
            HttpServletRequest request,
            HttpServletResponse response
    ) {

        NaverOAuthTokenResponse naverAuth = restClient.requestNaverAuth(code, redirect+"/login");

        // 2. 네이버 유저 정보 요청
        NaverUserInfoResponse naverUserInfo =  restClient.requestNaverUser(naverAuth.getAccess_token());
        String userUuid = naverUserInfo.getResponse().getId(); // 네이버 유저 고유 ID

        // 3. 회원인지 확인
        if (accountService.isRegisteredUser(userUuid)) {
            Account userByUuid = accountService.getUserByUuid(userUuid);

            // 4. 토큰 생성
            AuthTokenDto token = authService.createAccessToken(userByUuid);

            // 5. 쿠키에 저장 ()
            Cookie refreshCookie = new Cookie("refresh_token", token.getRefreshToken());
            refreshCookie.setHttpOnly(true);
            refreshCookie.setSecure(false);
            refreshCookie.setMaxAge(60 * 60 * 24);
            refreshCookie.setPath("/");
            response.addCookie(refreshCookie);

            // 6. 응답 반환
            return ApiResult.ok(userByUuid);
        }

        // 7. 회원이 아닌 경우 회원가입 처리
        Account newAccount = accountService.registerAccount(
                naverUserInfo.getResponse().getEmail(),
                userUuid,
                LoginType.FACEBOOK
        );

        return ApiResult.created(newAccount);

    }

    @GetMapping("/callback/google")
    public ApiResult<Account> gooogleAuthCallback(
            @RequestParam("code") String code,
            @RequestParam("redirect") String redirect,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        GoogleOAuthTokenResponse googleAuth = restClient.requestGoogleAuth(code, redirect+"/login");
        GoogleUserInfoResponse googleUserInfoResponse = restClient.requestGoogleGetUser(googleAuth.getAccessToken());
        String userUuid = googleUserInfoResponse.getSub();
        if (accountService.isRegisteredUser(userUuid)) {
            // TODO : 계정 정지 등에 대한 분기 처리
            Account userByUuid = accountService.getUserByUuid(userUuid);
            AuthTokenDto token = authService.createAccessToken(userByUuid);
            HttpHeaders headers = new HttpHeaders();
            Cookie refreshCookie = new Cookie("refresh_token", token.getRefreshToken());
            refreshCookie.setHttpOnly(true); // JS에서 읽을 수 있게 x
            refreshCookie.setSecure(true);   // HTTPS 아니어도 허용
            refreshCookie.setPath("/");
            refreshCookie.setMaxAge(60 * 60 * 240); // 1일 (초 단위)
            response.addCookie(refreshCookie);

            response.addHeader("Authorization", "Bearer " + token.getAccessToken());
            return ApiResult.ok(userByUuid);
        }
        return ApiResult.created(accountService.registerAccount(googleUserInfoResponse.getEmail(), userUuid, LoginType.GOOGLE));
    }


    @GetMapping("/callback/kakao")
    public ApiResult<Account> kakaoAuthCallback(
            @RequestParam("code") String code,
            @RequestParam("redirect") String redirect,
            HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 인증코드로 액세스토큰 발급
        KakaoAuthResponse s = restClient.requestKakaoAuth(redirect+"/login", code);
        // id 토큰을 파싱하여 aud 추출( 카카오톡 유저별 고유 아이디 )
        KaKaoUserPayload kaKaoUserPayload = jwtProvider.parsingJwtBody(s.getIdToken(), KaKaoUserPayload.class);
        // 가입한적이 있는 유저의 경우 유저정보 리턴
        String userUuid = kaKaoUserPayload.getAud();
        // 가입된 적이 있는지
        if (accountService.isRegisteredUser(userUuid)) {
            // TODO : 계정 정지 등에 대한 분기 처리
            Account userByUuid = accountService.getUserByUuid(userUuid);
            AuthTokenDto token = authService.createAccessToken(userByUuid);
            HttpHeaders headers = new HttpHeaders();
          /*  Cookie refreshCookie = new Cookie("refresh_token", token.getRefreshToken());
            refreshCookie.setHttpOnly(true); // JS에서 읽을 수 있게 x
            refreshCookie.setSecure(true);   // HTTPS 아니어도 허용
            refreshCookie.setPath("/");
            refreshCookie.setMaxAge(60 * 60 * 240); // 1일 (초 단위)*/
            ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", token.getRefreshToken())
            .httpOnly(true)
            .secure(true)       // HTTPS에서만 전송
            .path("/")
                .domain("unknown-auction.shop") 
            .maxAge(60 * 60 * 24) // 1일
            .sameSite("None")   // SameSite=None 설정!
            .build();

    // 👇 헤더에 바로 추가
    response.addHeader("Set-Cookie", refreshCookie.toString());
            //response.addCookie(refreshCookie);

            return ApiResult.ok(userByUuid);
        }
        // 가입된 적이 없다면 기본적인 정보 가져와서 계정 생성
        KakaoUserResponse kakaoUserResponse = restClient.requestKakaoGetUserApi(s.getAccessToken());
        log.info(kakaoUserResponse.toString());
        String kakaoLinkedEmail = kakaoUserResponse.getKakaoAccount().getEmail();

        // 서비스 자체 회원가입이 필요해서 필수정보만 넘겨주기
        return ApiResult.created(accountService.registerAccount(kakaoLinkedEmail, userUuid, LoginType.KAKAO));
    }
}
