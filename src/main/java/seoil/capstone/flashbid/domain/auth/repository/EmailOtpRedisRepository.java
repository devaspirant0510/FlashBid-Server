package seoil.capstone.flashbid.domain.auth.repository;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
@Slf4j
@RequiredArgsConstructor
public class EmailOtpRedisRepository {
    private final RedisTemplate<String, String> redisTemplate;
    private static final String OTP_PREFIX = "email-otp:";

    private String generateRandomOtp(){
        StringBuilder otp = new StringBuilder();
        for(int i=0; i<6; i++){
            int digit = (int)(Math.random() * 10);
            otp.append(digit);
        }
        return otp.toString();
    }
    public String generateOtp(String email){
        String otp = generateRandomOtp();
        log.info("redis otp: {}", otp);
        // 유효기간 10분
        redisTemplate.opsForValue().set(OTP_PREFIX + email, otp, Duration.ofMinutes(10));
        return otp;
    }
    public boolean validateOtp(String email, String otp){
        String storedOtp = redisTemplate.opsForValue().get(OTP_PREFIX + email);
        if(storedOtp != null && storedOtp.equals(otp)){
            // 검증 성공 시, OTP 삭제
            redisTemplate.delete(OTP_PREFIX + email);
            return true;
        }
        return false;
    }
}
