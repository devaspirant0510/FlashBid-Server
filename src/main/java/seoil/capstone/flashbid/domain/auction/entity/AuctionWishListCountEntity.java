package seoil.capstone.flashbid.domain.auction.entity;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "AuctionWishlistCount")
@Table(name = "auction_wishlist_count")
public class AuctionWishListCountEntity {
    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private Auction auction;

    private Long count;

    public static AuctionWishListCountEntity init(Auction auction) {
        AuctionWishListCountEntity entity = new AuctionWishListCountEntity();
        entity.setAuction(auction);
        entity.setCount(0L);
        return entity;
    }

}
