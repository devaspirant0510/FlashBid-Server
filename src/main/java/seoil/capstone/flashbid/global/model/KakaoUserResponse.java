package seoil.capstone.flashbid.global.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@ToString
public class KakaoUserResponse {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("has_signed_up")
    private Boolean hasSignedUp;

    @JsonProperty("connected_at")
    private LocalDateTime connectedAt;

    @JsonProperty("synched_at")
    private LocalDateTime synchedAt;

    @JsonProperty("properties")
    private Map<String, Object> properties;

    @JsonProperty("kakao_account")
    public KakaoAccount kakaoAccount;

    @JsonProperty("for_partner")
    private Partner forPartner;

    // Getters & Setters
}

class Profile {
    @JsonProperty("nickname")
    private String nickname;

    @JsonProperty("thumbnail_image_url")
    private String thumbnailImageUrl;

    @JsonProperty("profile_image_url")
    private String profileImageUrl;

    @JsonProperty("is_default_image")
    private Boolean isDefaultImage;

    @JsonProperty("is_default_nickname")
    private Boolean isDefaultNickname;

    // Getters & Setters
}

class Partner {
    @JsonProperty("uuid")
    private String uuid;

    // Getters & Setters
}