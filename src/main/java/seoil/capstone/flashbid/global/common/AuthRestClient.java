package seoil.capstone.flashbid.global.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import seoil.capstone.flashbid.global.model.*;

@Component
public class AuthRestClient {
    private static final Logger log = LoggerFactory.getLogger(AuthRestClient.class);
    private final RestTemplate restTemplate = new RestTemplate();
    @Value("${KAKAO_REST_API_KEY}")
    private String kakaoRestAPIKey;
    @Value("${GOOGLE_CLIENT_ID}")
    private String googleClientId;
    @Value("${GOOGLE_SECRETR_KEY}")
    private String googleSecretKey;
    @Value("${NAVER_CLIENT_ID}")
    private String naverClientId;
    @Value("${NAVER_SECRET_KEY}")
    private String naverClientSecret;

    public Object requestNaverAuth(String code,String redirectUri){
        String url = "https://nid.naver.com/oauth2.0/token";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();

        String grantType = "authorization_code";
        body.add("grant_type", grantType);
        body.add("client_id", naverClientId);
        body.add("client_secret", naverClientSecret);
        body.add("state","1234");

        switch (grantType) {
            case "authorization_code": // 발급
                body.add("code", code);
                // state가 필요하면 여기에 추가 가능 (보통 인증 요청 때 사용)
                break;
//            case "refresh_token": // 갱신
//                body.add("refresh_token", refreshToken);
//                break;
//            case "delete": // 삭제 (토큰 삭제)
//                // access_token은 URL 인코딩해서 넣어야 함
//                String encodedAccessToken = URLEncoder.encode(accessToken, StandardCharsets.UTF_8);
//                body.add("access_token", encodedAccessToken);
//                body.add("service_provider", "NAVER");
//                break;
            default:
                throw new IllegalArgumentException("Invalid grant_type: " + grantType);
        }

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<Object> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                Object.class
        );

        log.info("네이버 OAuth 응답: {}", response);
        return response.getBody();
    }
    public GoogleUserInfoResponse requestGoogleGetUser(String accessToken){
        String url ="https://openidconnect.googleapis.com/v1/userinfo";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken); // Authorization: Bearer {accessToken}

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<GoogleUserInfoResponse> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                request,
                GoogleUserInfoResponse.class
        );
        log.info(response.toString());
        return response.getBody();
    }


    public GoogleOAuthTokenResponse requestGoogleAuth(String code, String redirectUri) {
        String url = "https://oauth2.googleapis.com/token";

        // 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // 요청 본문 설정
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("code", code);
        body.add("client_id", googleClientId);
        body.add("client_secret", googleSecretKey);
        body.add("redirect_uri", redirectUri);
        body.add("grant_type", "authorization_code");

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<GoogleOAuthTokenResponse> response = restTemplate.exchange(
                url, HttpMethod.POST, requestEntity, GoogleOAuthTokenResponse.class
        );

        log.info("구글 토큰 응답: {}", response);
        return response.getBody();
    }

    public KakaoAuthResponse requestKakaoAuth(String redirectUri, String authorizeCode) {
        String url = "https://kauth.kakao.com/oauth/token";

        // 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // 요청 본문 설정 (MultiValueMap 사용)
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoRestAPIKey);
        body.add("redirect_uri", redirectUri);
        body.add("code", authorizeCode);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

        // 요청 보내기
        ResponseEntity<KakaoAuthResponse> response = restTemplate.exchange(
                url, HttpMethod.POST, requestEntity, KakaoAuthResponse.class
        );
        return response.getBody();
    }
    public KakaoUserResponse requestKakaoGetUserApi(String accessToken){
        String url = "https://kapi.kakao.com/v2/user/me";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<KakaoUserResponse> response = restTemplate.exchange(
                url, HttpMethod.GET, requestEntity, KakaoUserResponse.class
        );
        log.info(response.toString());
        return response.getBody();

    }
}
