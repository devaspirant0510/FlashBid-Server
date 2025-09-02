package seoil.capstone.flashbid.domain.admin.projection;

import java.time.LocalDateTime;

public interface BidInfoProjection {
    String getTitle();
    Long getAuctionId();
    LocalDateTime getBidAt();
    Long getCurrentPrice();
    LocalDateTime getAuctionCreatedAt();
    Long getStartPrice();




}
