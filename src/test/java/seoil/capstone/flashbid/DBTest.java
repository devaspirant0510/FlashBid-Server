package seoil.capstone.flashbid;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import seoil.capstone.flashbid.domain.user.entity.Account;
import seoil.capstone.flashbid.domain.user.repository.AccountRepository;
import seoil.capstone.flashbid.global.common.enums.LoginType;
import seoil.capstone.flashbid.global.common.enums.UserStatus;
import seoil.capstone.flashbid.global.common.enums.UserType;
import seoil.capstone.flashbid.global.core.provider.HashProvider;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Date;

@SpringBootTest
public class DBTest {
    @Autowired
    private  AccountRepository accountRepository;
    @Autowired
    private HashProvider hashProvier;
    @Test
    public void insertUserData() {
        long ACCESS_TOKEN_EXPIRATION = 1000L * 60 * 60 * 24 * 365;

        SecretKey signedKey = hashProvier.getSignedKey("3bd56aea9642eff83cb0e2fb20d95b4e3bd56aea9642eff83cb0e2fb20d95b4e");
        String jwt = Jwts.builder()
                .setSubject("112295470980434292495")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION))
                .claim("id", 5)
                .claim("nickname","T1 도란")
                .claim("uid", "112295470980434292495")
                .claim("email", "seungho020510@gmail.com")
                .claim("role", "top gap")
                .signWith(signedKey, SignatureAlgorithm.HS256)
                .compact();
        System.out.println(jwt);
//        Account account = new Account(LoginType.KAKAO, UserStatus.ACTIVE, UserType.CUSTOMER, "seungho020510@gmail.com", LocalDateTime.now(), LocalDateTime.now(), true, "1000");
//        accountRepository.save(account);
    }
}
