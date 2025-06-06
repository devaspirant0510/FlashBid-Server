package seoil.capstone.flashbid.domain.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import seoil.capstone.flashbid.domain.user.dto.request.RegisterUserDto;
import seoil.capstone.flashbid.domain.user.entity.Player;
import seoil.capstone.flashbid.domain.user.service.AccountService;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
public class AccountController {
    private final AccountService accountService;

    @PostMapping("/user")
    public Player userRegister(@RequestBody RegisterUserDto userDto){

        log.info(String.format("body :%s",userDto.toString()));

        // 가입한 적이 있는 계정일경우 가입 막기
        if(accountService.isRegisterPlayer(userDto.getId())){
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
        return accountService.registerPlayer(userDto.getId(),userDto.getUid(),userDto.getUserName());
    }
}
