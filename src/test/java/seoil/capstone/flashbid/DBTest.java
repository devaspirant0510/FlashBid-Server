package seoil.capstone.flashbid;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import seoil.capstone.flashbid.domain.auction.projection.BidLoggingProjection;
import seoil.capstone.flashbid.domain.auction.repository.AuctionBidLogRepository;
import seoil.capstone.flashbid.domain.user.repository.AccountRepository;
import seoil.capstone.flashbid.global.core.provider.HashProvider;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

//@SpringBootTest
public class DBTest {

//    @Autowired
//    private  AccountRepository accountRepository;
//    @Autowired
//    private HashProvider hashProvier;
//    @Test
    public void insertUserData() {
//        long expirationMillis = 1000L * 60 * 60 * 24 * 365; //
//        Date now = new Date();
//        Date exp = new Date(now.getTime() + expirationMillis);
//
//        SecretKey signedKey = hashProvier.getSignedKey("3bd56aea9642eff83cb0e2fb20d95b4e3bd56aea9642eff83cb0e2fb20d95b4e");
//        String jwt = Jwts.builder()
//                .setSubject("b4ff0229451d8fc4f98b0c2a6545da33")
//                .setIssuedAt(now)
//                .setExpiration(exp)
//                .claim("id", "1")
//                .claim("uid", "b4ff0229451d8fc4f98b0c2a6545da33")
//                .claim("email", "1234")
//                .claim("role", "1234")
//                .signWith(signedKey, SignatureAlgorithm.HS256)
//                .compact();
//        System.out.println(jwt);
//        Account account = new Account(LoginType.KAKAO, UserStatus.ACTIVE, UserType.CUSTOMER, "seungho020510@gmail.com", LocalDateTime.now(), LocalDateTime.now(), true, "1000");
//        accountRepository.save(account);
    }
}
