package seoil.capstone.flashbid.domain.user.dto.response;

import lombok.Getter;
import seoil.capstone.flashbid.domain.user.entity.Account;

@Getter
public class FollowUserDto {
    private final Long id;
    private final String nickname;
    private final String profileUrl;

    private FollowUserDto(Long id, String nickname, String profileUrl) {
        this.id = id;
        this.nickname = nickname;
        this.profileUrl = profileUrl;
    }

    public static FollowUserDto from(Account account) {
        return new FollowUserDto(
                account.getId(),
                account.getNickname(),
                account.getProfileUrl()
        );
    }
}