package seoil.capstone.flashbid.domain.auction.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import seoil.capstone.flashbid.domain.user.entity.Account;
import seoil.capstone.flashbid.global.core.BaseTimeOnlyCreated;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "auction_wishlist",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"auction_id", "user_id"})
        }
)
public class AuctionWishListEntity extends BaseTimeOnlyCreated {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Account user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Auction auction;
}
