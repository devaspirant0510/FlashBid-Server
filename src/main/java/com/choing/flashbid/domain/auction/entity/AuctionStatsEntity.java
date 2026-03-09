package com.choing.flashbid.domain.auction.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "AuctionStats")
@Table(name = "auction_stats")
public class AuctionStatsEntity {
    @Id
    private Long id;

    private Long biddingCount;
    private Long lastBidAmount;
    private LocalDateTime lastBidTime;
    private Long participantsCount;

    @OneToOne
    private Auction auction;

    public static AuctionStatsEntity init(Auction auction) {
        return AuctionStatsEntity.builder()
                .id(auction.getId())
                .auction(auction)
                .biddingCount(0L)
                .lastBidAmount(0L)
                .lastBidTime(null)
                .participantsCount(0L)
                .build();

    }
}
