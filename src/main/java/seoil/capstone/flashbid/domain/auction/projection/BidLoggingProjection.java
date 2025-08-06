package seoil.capstone.flashbid.domain.auction.projection;

import java.time.LocalDateTime;

public interface BidLoggingProjection {
    Long getId();
    LocalDateTime getCreatedAt();
    Long getPrevPrice();
    Long getPrice();
    Long getBidderId();
    String getBidderName();
    String getProfileUrl();
}
