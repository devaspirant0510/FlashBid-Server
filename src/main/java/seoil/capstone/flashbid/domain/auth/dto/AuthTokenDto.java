package seoil.capstone.flashbid.domain.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AuthTokenDto {
    private  String accessToken;
    private String refreshToken;
}
