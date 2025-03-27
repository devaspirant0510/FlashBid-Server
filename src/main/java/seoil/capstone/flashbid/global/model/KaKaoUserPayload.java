package seoil.capstone.flashbid.global.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class KaKaoUserPayload {
    private String aud;
    private String sub;
    @JsonProperty("auth_time")
    private long authTime;
    private String iss;
    private long exp;
    private long iat;
    private String nickname;
    private String picture;
    private String email;
}
