package seoil.capstone.flashbid.domain.feed.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.*;
import seoil.capstone.flashbid.domain.auction.entity.Auction;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class FeedAuctionEntity {
    @Id
    private Long id;

    @ManyToOne
    private FeedEntity feed;

    @ManyToOne
    private Auction auction;
}
