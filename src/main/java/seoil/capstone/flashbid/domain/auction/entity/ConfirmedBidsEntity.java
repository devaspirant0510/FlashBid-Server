package seoil.capstone.flashbid.domain.auction.entity;

import jakarta.persistence.*;
import lombok.*;
import seoil.capstone.flashbid.domain.user.entity.Account;
import seoil.capstone.flashbid.global.core.BaseTimeEntity;
import seoil.capstone.flashbid.global.core.BaseTimeOnlyCreated;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "confirm_bids")
public class ConfirmedBidsEntity extends BaseTimeOnlyCreated {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Auction auction;

    @ManyToOne
    private Account bidder;

    @ManyToOne
    private Account seller;

    @ManyToOne
    private BiddingLogEntity biddingLog;
}
