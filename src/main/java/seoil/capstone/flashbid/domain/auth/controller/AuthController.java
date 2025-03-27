package seoil.capstone.flashbid.domain.auth.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import seoil.capstone.flashbid.domain.auth.dto.AuthTokenDto;
import seoil.capstone.flashbid.domain.auth.service.AuthService;
import seoil.capstone.flashbid.domain.user.entity.Account;
import seoil.capstone.flashbid.domain.user.service.AccountService;
import seoil.capstone.flashbid.global.common.AuthRestClient;
import seoil.capstone.flashbid.global.core.provider.JwtProvider;
import seoil.capstone.flashbid.global.model.KaKaoUserPayload;
import seoil.capstone.flashbid.global.model.KakaoAuthResponse;
import seoil.capstone.flashbid.global.model.KakaoUserResponse;

@Slf4j
@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthRestClient restClient;
    private final AccountService accountService;
    private final JwtProvider jwtProvider;
    private final AuthService authService;

    //TODO : redis 연동한 rfr 로직

    @GetMapping("/callback/kakao")
    public ResponseEntity<Account> kakaoAuthCallback(@RequestParam("code") String code) {
        // 인증코드로 액세스토큰 발급
        KakaoAuthResponse s = restClient.requestKakaoAuth("http://localhost:8080/auth/callback/kakao", code);
        // id 토큰을 파싱하여 aud 추출( 카카오톡 유저별 고유 아이디 )
        KaKaoUserPayload kaKaoUserPayload = jwtProvider.parsingJwtBody(s.getIdToken(), KaKaoUserPayload.class);
        // 가입한적이 있는 유저의 경우 유저정보 리턴
        String userUuid = kaKaoUserPayload.getAud();
        // 가입된 적이 있는지
        if (accountService.isRegisteredUser(userUuid)) {
            // TODO : 계정 정지 등에 대한 에러 처리
            Account userByUuid = accountService.getUserByUuid(userUuid);
            AuthTokenDto token = authService.createAccessToken(userByUuid);
            userByUuid.setEmail(token.getAccessToken());
            userByUuid.setUuid(token.getRefreshToken());
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization","Bearer "+token.getAccessToken());
            ResponseEntity<Account> accountResponseEntity = new ResponseEntity<>(userByUuid, headers,HttpStatus.OK);
            return accountResponseEntity;
        }
        // 가입된 적이 없다면 기본적인 정보 가져와서 계정 생성
        KakaoUserResponse kakaoUserResponse = restClient.requestKakaoGetUserApi(s.getAccessToken());
        log.info(kakaoUserResponse.toString());
        String kakaoLinkedEmail = kakaoUserResponse.getKakaoAccount().getEmail();
        return null ;//accountService.registerAccount(kakaoLinkedEmail,userUuid, LoginType.KAKAO);
    }
}
