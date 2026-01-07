package seoil.capstone.flashbid.domain.auction.entity;

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
    private Long chatCount;

    @OneToOne
    private Auction auction;
}
