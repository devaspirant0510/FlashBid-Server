package seoil.capstone.flashbid.infrastructure.kafka.event.payload;


import seoil.capstone.flashbid.infrastructure.kafka.event.EventPayload;

import java.time.LocalDateTime;

public class BidCreatedEventPayload implements EventPayload {
    Long bidId;
    Long auctionId;
    Long bidderId;
    Long sellerId;
    Long amount;
    Long currentPrice;
    Long startPrice;
    LocalDateTime startTime;
    LocalDateTime endTime;
    LocalDateTime createdAt;
}
