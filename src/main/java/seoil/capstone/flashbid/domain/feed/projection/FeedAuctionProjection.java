package seoil.capstone.flashbid.domain.feed.projection;

import com.google.type.DateTime;
import seoil.capstone.flashbid.domain.file.projection.FileProjection;

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
