package com.choing.flashbid.domain.auction.entity;

import jakarta.persistence.*;
import lombok.*;
import com.choing.flashbid.domain.user.entity.Account;
import com.choing.flashbid.global.core.BaseTimeOnlyCreated;


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
