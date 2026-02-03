package seoil.capstone.flashbid.domain.feed.projection;


import seoil.capstone.flashbid.global.common.enums.AuctionType;

import java.time.LocalDateTime;

public interface FeedConfirmBidsProjection {
    Long getConfirmBidId();

    Long getAuctionId();

    AuctionType getAuctionType();

    String getAuctionTitle();

    String getAuctionDescription();

    String getAuctionCategoryName();

    Integer getAuctionStartPrice();

    LocalDateTime getAuctionStartTime();

    LocalDateTime getAuctionEndTime();

    Long getBidConfirmedPrice();
    String getBidderName();
    String getBidderProfileImage();

    Integer getBiddingCount();
    Integer getChatCount();
}
