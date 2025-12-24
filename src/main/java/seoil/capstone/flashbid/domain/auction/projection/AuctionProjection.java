package seoil.capstone.flashbid.domain.auction.projection;

import java.time.LocalDateTime;

public interface AuctionProjection {
    Long getId();
    String getGoodsTitle();
    String getGoodsImageUrl();
    String getCategoryName();
    String getBidderName();
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
}
