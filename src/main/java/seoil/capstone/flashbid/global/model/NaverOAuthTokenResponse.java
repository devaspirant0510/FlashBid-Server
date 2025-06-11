package seoil.capstone.flashbid.global.model;


import lombok.Data;

@Data
public class NaverOAuthTokenResponse {
    private String access_token;
    private String refresh_token;
    private String token_type;
    private String expires_in;
}
