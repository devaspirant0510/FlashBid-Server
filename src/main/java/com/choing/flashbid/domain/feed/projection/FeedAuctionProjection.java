package com.choing.flashbid.domain.feed.projection;

import java.time.LocalDateTime;

public interface FeedAuctionProjection {
    Long getAuctionId();
    String getAuctionTitle();
    String getAuctionDescription();
    String getCategoryName();
    String getThumbnail();
    Long startPrice();
    Long currentPrice();
    LocalDateTime getStartTime();
    LocalDateTime getEndTime();
}
