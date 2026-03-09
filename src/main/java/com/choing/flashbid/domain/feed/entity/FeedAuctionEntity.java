package com.choing.flashbid.domain.feed.entity;

import jakarta.persistence.*;
import lombok.*;
import com.choing.flashbid.domain.auction.entity.Auction;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "FeedAuction")
@Table(name = "feed_auction")
public class FeedAuctionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private FeedEntity feed;

    @ManyToOne
    private Auction auction;
}
