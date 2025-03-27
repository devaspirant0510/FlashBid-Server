package seoil.capstone.flashbid.global.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import seoil.capstone.flashbid.global.model.KaKaoUserPayload;
import seoil.capstone.flashbid.global.model.KakaoAuthResponse;
import seoil.capstone.flashbid.global.model.KakaoUserResponse;

@Component
public class AuthRestClient {
    private static final Logger log = LoggerFactory.getLogger(AuthRestClient.class);
    private final RestTemplate restTemplate = new RestTemplate();
    @Value("${KAKAO_REST_API_KEY}")
    private String kakaoRestAPIKey;

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
