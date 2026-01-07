package seoil.capstone.flashbid.domain.feed.projection;

import seoil.capstone.flashbid.global.common.enums.AuctionStatus;
import seoil.capstone.flashbid.global.common.enums.AuctionType;

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

}
