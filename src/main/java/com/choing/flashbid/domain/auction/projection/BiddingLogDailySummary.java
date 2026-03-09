package com.choing.flashbid.domain.auction.projection;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface BiddingLogDailySummary {
    LocalDate getTruncatedDate();
    Long getCount();
    Long getTotalPrice();
}
