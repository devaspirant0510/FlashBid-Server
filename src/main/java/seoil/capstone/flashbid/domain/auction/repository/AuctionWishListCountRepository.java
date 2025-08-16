package seoil.capstone.flashbid.domain.auction.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import seoil.capstone.flashbid.domain.auction.entity.AuctionWishListCountEntity;

public interface AuctionWishListCountRepository extends JpaRepository<AuctionWishListCountEntity,Long> {
    AuctionWishListCountEntity findByAuctionId(Long auctionId);

    @Modifying
    @Query("UPDATE AuctionWishlistCount c SET c.count = c.count + 1 WHERE c.auction.id = :auctionId")
    int incrementCountByAuctionId(Long auctionId);
}
