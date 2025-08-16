package seoil.capstone.flashbid.domain.user.projection;

public interface AccountStatusInfoProjection {
    String getNickname();

    String getProfileUrl();

    Long getBiddingCount();

    Long getSellCount();

    Long getReviewCount();

    Long getFollowerCount();

    Long getFollowingCount();

}
