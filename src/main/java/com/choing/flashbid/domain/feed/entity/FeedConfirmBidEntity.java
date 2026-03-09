package com.choing.flashbid.domain.feed.entity;

import jakarta.persistence.*;
import lombok.*;
import com.choing.flashbid.domain.auction.entity.ConfirmedBidsEntity;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "FeedConfirmBid")
@Table(name = "feed_confirm_bid")
public class FeedConfirmBidEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private FeedEntity feed;

    @ManyToOne
    private ConfirmedBidsEntity confirmedBids;
}
