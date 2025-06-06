package seoil.capstone.flashbid.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import seoil.capstone.flashbid.domain.auth.dto.AuthTokenDto;
import seoil.capstone.flashbid.domain.user.entity.Account;
import seoil.capstone.flashbid.global.core.provider.JwtProvider;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtProvider jwtProvider;

    public AuthTokenDto createAccessToken(Account account) {
        String accessToken = jwtProvider.createAccessToken(account.getUuid(), account);
        String refreshToken = jwtProvider.createRefreshToken(account.getUuid());
        return new AuthTokenDto(
                accessToken,
                refreshToken
        );
    }


    public void reGenRefreshToken() {

    }
}
