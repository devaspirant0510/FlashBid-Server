package seoil.capstone.flashbid.domain.auth.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import seoil.capstone.flashbid.domain.auth.dto.AuthTokenDto;
import seoil.capstone.flashbid.domain.auth.dto.RegisterDto;
import seoil.capstone.flashbid.domain.auth.dto.RegisterEmailDto;
import seoil.capstone.flashbid.domain.auth.service.AuthService;
import seoil.capstone.flashbid.domain.user.entity.Account;
import seoil.capstone.flashbid.domain.user.service.AccountService;
import seoil.capstone.flashbid.global.common.AuthRestClient;
import seoil.capstone.flashbid.global.common.enums.LoginType;
import seoil.capstone.flashbid.global.common.response.ApiResult;
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


    @PostMapping("/register/oauth")
    public ApiResult<Account> registerService(@RequestBody RegisterDto dto,HttpServletRequest request){
        //TODO : 가입 여부 확인
        return ApiResult.ok(authService.registerUser(dto),request);
    }
    @PostMapping("/register/email")
    public ApiResult<Account> registerEmail(@RequestBody RegisterEmailDto dto, HttpServletRequest request){
        return ApiResult.ok(authService.registerUserWithEmail(dto),request);

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

            // 5. 쿠키에 저장 (JS에서 접근 가능하도록 보안 옵션 약하게)
            Cookie refreshCookie = new Cookie("refresh_token", token.getRefreshToken());
            refreshCookie.setHttpOnly(false);
            refreshCookie.setSecure(false);
            refreshCookie.setMaxAge(60 * 60 * 24);
            refreshCookie.setPath("/");
            response.addCookie(refreshCookie);

            Cookie accessCookie = new Cookie("access_token", token.getAccessToken());
            accessCookie.setHttpOnly(false);
            accessCookie.setSecure(false);
            accessCookie.setMaxAge(60 * 60 * 24);
            accessCookie.setPath("/");
            response.addCookie(accessCookie);
            response.addHeader("Authorization", "Bearer " + token.getAccessToken());

            // 6. 응답 반환
            return ApiResult.ok(userByUuid, request);
        }

        // 7. 회원이 아닌 경우 회원가입 처리
        Account newAccount = accountService.registerAccount(
                naverUserInfo.getResponse().getEmail(),
                userUuid,
                LoginType.FACEBOOK
        );

        return ApiResult.created(newAccount, request);

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
            refreshCookie.setHttpOnly(false); // JS에서 읽을 수 있게
            refreshCookie.setSecure(false);   // HTTPS 아니어도 허용
            refreshCookie.setMaxAge(60 * 60 * 240); // 1일 (초 단위)
            response.addCookie(refreshCookie);

            Cookie accessCookie = new Cookie("access_token", token.getAccessToken());
            accessCookie.setHttpOnly(false);
            accessCookie.setSecure(false);
            accessCookie.setMaxAge(60 * 60 * 240);
            response.addCookie(accessCookie);
            response.addHeader("Authorization", "Bearer " + token.getAccessToken());
            return ApiResult.ok(userByUuid, request);
        }
        return ApiResult.created(accountService.registerAccount(googleUserInfoResponse.getEmail(), userUuid, LoginType.KAKAO), request);
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
            Cookie refreshCookie = new Cookie("refresh_token", token.getRefreshToken());
            refreshCookie.setHttpOnly(false); // JS에서 읽을 수 있게
            refreshCookie.setSecure(false);   // HTTPS 아니어도 허용
            refreshCookie.setMaxAge(60 * 60 * 240); // 1일 (초 단위)
            response.addCookie(refreshCookie);

            Cookie accessCookie = new Cookie("access_token", token.getAccessToken());
            accessCookie.setHttpOnly(false);
            accessCookie.setSecure(false);
            accessCookie.setMaxAge(60 * 60 * 24*10);
            response.addCookie(accessCookie);
            response.addHeader("Authorization", "Bearer " + token.getAccessToken());
            return ApiResult.ok(userByUuid, request);
        }
        // 가입된 적이 없다면 기본적인 정보 가져와서 계정 생성
        KakaoUserResponse kakaoUserResponse = restClient.requestKakaoGetUserApi(s.getAccessToken());
        log.info(kakaoUserResponse.toString());
        String kakaoLinkedEmail = kakaoUserResponse.getKakaoAccount().getEmail();

        // 서비스 자체 회원가입이 필요해서 필수정보만 넘겨주기
        return ApiResult.created(accountService.registerAccount(kakaoLinkedEmail, userUuid, LoginType.KAKAO), request);
    }
}
