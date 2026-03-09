package com.choing.flashbid.domain.auction.projection;

import java.time.LocalDateTime;

public interface AuctionDetailProjection {
    Long getId();
    String getTitle();
    String getCategoryName();
    String getDescription();
    Long getCurrentPrice();
    Long getStartPrice();
    Integer getParticipateCount();
    Integer getBiddingCount();
    Long getViewCount();
    Long getLikeCount();
    Long getChatMessagingCount();
    LocalDateTime getStartTime();
    LocalDateTime getEndTime();
    Short getStatus();
    Short getAuctionType();
    Boolean getIsLiked();

    Long getBidderId();
    String getBidderNickname();
    Integer getBidderFollower();
    Integer getBidderFollowing();
    Integer getBidderConfirmBidCount();
    Integer getBidderSaleCount();
}
