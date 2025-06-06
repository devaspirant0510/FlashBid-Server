package seoil.capstone.flashbid;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import seoil.capstone.flashbid.domain.user.entity.Account;
import seoil.capstone.flashbid.domain.user.repository.AccountRepository;
import seoil.capstone.flashbid.global.common.enums.LoginType;
import seoil.capstone.flashbid.global.common.enums.UserStatus;
import seoil.capstone.flashbid.global.common.enums.UserType;

import java.time.LocalDateTime;

@SpringBootTest
public class DBTest {
    @Autowired
    private  AccountRepository accountRepository;
    @Test
    public void insertUserData() {
        Account account = new Account(LoginType.KAKAO, UserStatus.ACTIVE, UserType.CUSTOMER, "seungho020510@gmail.com", LocalDateTime.now(), LocalDateTime.now(), true, "1000");
        accountRepository.save(account);
    }
}
