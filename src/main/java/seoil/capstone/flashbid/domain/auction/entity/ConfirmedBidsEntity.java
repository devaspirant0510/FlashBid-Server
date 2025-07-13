package seoil.capstone.flashbid.domain.auction.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.*;
import seoil.capstone.flashbid.domain.user.entity.Account;
import seoil.capstone.flashbid.global.core.BaseTimeEntity;
import seoil.capstone.flashbid.global.core.BaseTimeOnlyCreated;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class ConfirmedBidsEntity extends BaseTimeOnlyCreated {
    @Id
    private Long id;

    @ManyToOne
    private Goods goods;

    @ManyToOne
    private Account bidder;

    @ManyToOne
    private Account seller;

    @ManyToOne
    private BiddingLogEntity biddingLog;
}
