package com.choing.flashbid.domain.user.dto.response;

import lombok.*;
import com.choing.flashbid.domain.user.entity.Account;
import com.choing.flashbid.global.common.enums.UserStatus;
import com.choing.flashbid.global.common.enums.UserType;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {
    private Long id;
    private String email;
    private String nickname;
    private String profileUrl;
    private Integer point;
    private UserType userType;
    private UserStatus userStatus;

    public static AccountDto from(Account account) {
        AccountDto accountDto = new AccountDto();
        accountDto.setId(account.getId());
        accountDto.setEmail(account.getEmail());
        accountDto.setNickname(account.getNickname());
        accountDto.setProfileUrl(account.getProfileUrl());
        accountDto.setPoint(account.getPoint());
        accountDto.setUserType(account.getUserType());
        accountDto.setUserStatus(account.getUserStatus());
        return accountDto;
    }



}
