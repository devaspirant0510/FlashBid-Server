package seoil.capstone.flashbid.domain.feed.entity;

import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import jakarta.persistence.Entity;
import seoil.capstone.flashbid.domain.auction.entity.ConfirmedBidsEntity;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class FeedConfirmBidEntity {
    @Id
    private Long id;

    @ManyToOne
    private FeedEntity feed;

    @ManyToOne
    private ConfirmedBidsEntity confirmedBids;
}
