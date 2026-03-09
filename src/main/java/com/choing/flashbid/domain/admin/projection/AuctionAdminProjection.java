package com.choing.flashbid.domain.admin.projection;

import java.time.LocalDateTime;

public interface AuctionAdminProjection {
    Long getAuctionId();

    String getGoodsTitle();

    String getSellerNickname();

    String getCategoryName();

    Integer getStartPrice();

    Long getLastBidAmount();

    Long getBiddingCount();

    Long getParticipantsCount();

    Long getChatCount();

    Integer getViewCount();

    Integer getAuctionType();

    Integer getAuctionStatus();

    LocalDateTime getStartTime();

    LocalDateTime getEndTime();

    LocalDateTime getCreatedAt();
}
