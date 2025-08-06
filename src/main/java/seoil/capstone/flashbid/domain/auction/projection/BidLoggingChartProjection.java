package seoil.capstone.flashbid.domain.auction.projection;

import java.time.LocalDateTime;

public interface BidLoggingChartProjection {
    Long getId();
    LocalDateTime getCreatedAt();
    Long getPrice();
}
