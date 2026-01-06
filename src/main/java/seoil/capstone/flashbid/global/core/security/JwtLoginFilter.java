package seoil.capstone.flashbid.global.core.security;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import seoil.capstone.flashbid.domain.auth.dto.AuthTokenDto;
import seoil.capstone.flashbid.domain.auth.dto.EmailAuthLoginDto;
import seoil.capstone.flashbid.domain.auth.service.AuthService;
import seoil.capstone.flashbid.domain.user.dto.response.AccountDto;
import seoil.capstone.flashbid.domain.user.entity.Account;
import seoil.capstone.flashbid.domain.user.repository.AccountRepository;
import seoil.capstone.flashbid.global.common.error.ApiException;
import seoil.capstone.flashbid.global.common.response.ApiResult;
import seoil.capstone.flashbid.global.core.provider.CookieProvider;

import java.io.IOException;

public class JwtLoginFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final AuthService authService;
    private final CookieProvider cookieProvider;
    private final AccountRepository accountRepository;
    private final ObjectMapper objectMapper;

    public JwtLoginFilter(
            AuthenticationManager authenticationManager,
            AuthService authService,
            CookieProvider cookieProvider,
            AccountRepository accountRepository,
            ObjectMapper objectMapper
    ) {
        this.authenticationManager = authenticationManager;
        this.authService = authService;
        this.cookieProvider = cookieProvider;
        this.accountRepository = accountRepository;
        this.objectMapper = objectMapper;

        // 1. 로그인 시도시 POST /auth/login
        setFilterProcessesUrl("/auth/login");
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        // 2. 요청 객체 매핑해서  AUTH 객체 넘김 -> 3. CustomUserDetailService 로 이동(실제 디비에서 유효한계정인지 검증 + 해시화된 패스워드 검증)
        try {
            EmailAuthLoginDto loginDto = new ObjectMapper().readValue(request.getInputStream(), EmailAuthLoginDto.class);
            UsernamePasswordAuthenticationToken authInfo = new UsernamePasswordAuthenticationToken(
                    loginDto.getEmail(), loginDto.getPassword()
            );

            return authenticationManager.authenticate(authInfo);
        } catch (IOException e) {
            throw new ApiException(HttpStatus.BAD_REQUEST);

        }
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authResult
    ) throws IOException, ServletException {
        // 4. 검증 성공시 (3. CustomUserDetailService 에서 디비 조회후 성공시) 토큰 저장
        Account account = accountRepository.findByEmail(authResult.getName()).orElseThrow();
        AuthTokenDto jwtToken = authService.createJwtToken(account);

        response.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken.getAccessToken());

        ResponseCookie refreshCookie = cookieProvider.generateRefreshTokenCookie(jwtToken.getRefreshToken());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        // body
        AccountDto bodyData = AccountDto.from(account);
        ApiResult<AccountDto> body =
                ApiResult.ok(bodyData, "로그인 성공");

        response.setStatus(HttpStatus.OK.value());
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        response.getWriter().write(
                objectMapper.writeValueAsString(body)
        );
    }
}
