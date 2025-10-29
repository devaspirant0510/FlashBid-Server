package seoil.capstone.flashbid.domain.user.dto.response;

import lombok.Getter;
import seoil.capstone.flashbid.domain.file.entity.FileEntity;
import seoil.capstone.flashbid.domain.user.entity.Account;

@Getter
public class PublicProfileUserDto {
    private final Account user;
    private final int followerCount;
    private final int followingCount;
    private final int feedCount;
    private final FileEntity profileImage;
    private final boolean isFollowing; // [신규 필드]

    public PublicProfileUserDto(UserDto userDto, boolean isFollowing) {
        this.user = userDto.getUser();
        this.followerCount = userDto.getFollowerCount();
        this.followingCount = userDto.getFollowingCount();
        this.feedCount = userDto.getFeedCount();
        this.profileImage = userDto.getProfileImage();
        this.isFollowing = isFollowing;
    }
}