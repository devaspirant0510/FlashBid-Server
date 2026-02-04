package seoil.capstone.flashbid.domain.auction.entity;

import jakarta.persistence.*;
import lombok.*;
import seoil.capstone.flashbid.global.common.enums.AuctionEventType;
import seoil.capstone.flashbid.global.core.BaseTimeOnlyCreated;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "AuctionEventLog")
@Table(name = "auction_event_log")
public class AuctionEventLogEntity extends BaseTimeOnlyCreated {
    @Id
    private Long id;

    @Enumerated
    @Column(name = "auction_event_type", nullable = false, length = 20)
    private AuctionEventType auctionEventType;

    private LocalDateTime eventTime;
    private Boolean isProcessed;

    @ManyToOne
    private Auction auction;

}
