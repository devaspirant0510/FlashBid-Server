package seoil.capstone.flashbid.global.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import seoil.capstone.flashbid.domain.auth.filter.JwtAuthenticationFilter;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // CSRF 비활성화 (필요한 경우)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "auth/callback/*").permitAll()
                        .anyRequest().authenticated()
                ) // 모든 요청 허용
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class) // jwt 파싱해서 유효한 토큰인지 검증하는 필터
                .formLogin(AbstractHttpConfigurer::disable) // 기본 로그인 폼 제거
                .logout(AbstractHttpConfigurer::disable) // 로그아웃 제거
                .sessionManagement(session-> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 사용 안함
                .httpBasic(AbstractHttpConfigurer::disable); // HTTP Basic 인증 제거
        return http.build();

    }
}
