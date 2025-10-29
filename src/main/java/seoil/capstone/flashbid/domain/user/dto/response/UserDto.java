package seoil.capstone.flashbid.domain.user.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import seoil.capstone.flashbid.domain.file.entity.FileEntity;
import seoil.capstone.flashbid.domain.user.entity.Account;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    Account user;
    int followerCount;
    int followingCount;
    int feedCount;
    FileEntity profileImage;
    private boolean isFollowing;

    public UserDto(Account user, int followerCount, int followingCount, int feedCount, FileEntity profileImage) {
        this.user = user;
        this.followerCount = followerCount;
        this.followingCount = followingCount;
        this.feedCount = feedCount;
        this.profileImage = profileImage;
        this.isFollowing = false;
    }

    public static UserDto from(Account account) {
        return new UserDto(account, 0, 0, 0, null, false);
    }
}
