package com.choing.flashbid.domain.feed.projection;

import com.choing.flashbid.global.common.enums.AuctionStatus;
import com.choing.flashbid.global.common.enums.AuctionType;

import java.time.LocalDateTime;

public interface FeedSummaryProjection extends FeedProjection {
    Long getAuctionId();

    AuctionType getAuctionType();

    String getAuctionCategoryName();

    String getAuctionTitle();

    String getAuctionDescription();

    Integer getAuctionLikeCount();

    Integer getAuctionViewCount();

    Integer getAuctionStartPrice();

    Integer getAuctionCurrentPrice();

    AuctionStatus getAuctionStatus();

    LocalDateTime getAuctionStartTime();

    LocalDateTime getAuctionEndTime();

    String getAuctionImageUrl();

    Long getConfirmBidId();

    Long getConfirmBidAuctionId();

    AuctionType getConfirmBidAuctionType();

    String getConfirmBidAuctionTitle();

    String getConfirmBidAuctionDescription();

    String getConfirmBidAuctionCategoryName();

    Integer getConfirmBidAuctionStartPrice();

    LocalDateTime getConfirmBidAuctionStartTime();

    LocalDateTime getConfirmBidAuctionEndTime();

    Long getConfirmedBidPrice();
    String getConfirmedBidBidderName();
    String getConfirmedBidBidderProfileImage();

    Integer getConfirmedBidBiddingCount();
    Integer getConfirmedBidChatCount();
}
