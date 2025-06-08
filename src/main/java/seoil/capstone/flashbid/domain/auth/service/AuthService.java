package seoil.capstone.flashbid.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import seoil.capstone.flashbid.domain.auth.dto.AuthTokenDto;
import seoil.capstone.flashbid.domain.auth.dto.RegisterDto;
import seoil.capstone.flashbid.domain.user.entity.Account;
import seoil.capstone.flashbid.domain.user.repository.AccountRepository;
import seoil.capstone.flashbid.global.common.error.ApiException;
import seoil.capstone.flashbid.global.core.provider.JwtProvider;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtProvider jwtProvider;
    private final AccountRepository accountRepository;

    public AuthTokenDto createAccessToken(Account account) {
        String accessToken = jwtProvider.createAccessToken(account.getUuid(), account);
        String refreshToken = jwtProvider.createRefreshToken(account.getUuid());
        return new AuthTokenDto(
                accessToken,
                refreshToken
        );
    }

    @Transactional
    public Account registerUser(RegisterDto dto){
        Account account = accountRepository.findByUuid(dto.getUid()).orElseThrow(() ->
                new ApiException(HttpStatus.NOT_FOUND, "", ""));
        account.setNickname(dto.getNickname());
        return  account;
    }


    public void reGenRefreshToken() {

    }
}
