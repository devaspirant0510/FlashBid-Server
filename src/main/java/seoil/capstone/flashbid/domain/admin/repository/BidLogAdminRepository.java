package seoil.capstone.flashbid.domain.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import seoil.capstone.flashbid.domain.admin.projection.BidInfoProjection;
import seoil.capstone.flashbid.domain.auction.entity.BiddingLogEntity;

import java.util.List;

public interface BidLogAdminRepository extends JpaRepository<BiddingLogEntity, Long> {
    @Query("""
            SELECT gd.title as title,b.auction.id as auctionId,b.createdAt as bidAt,b.price as currentPrice,au.createdAt as auctionCreatedAt,au.startPrice as startPrice
            FROM bidding_log b
            join Auction au
            on au.id=b.auction.id
            join Goods gd
            on au.goods.id = gd.id
            WHERE b.createdAt = (
            	SELECT MAX(bl.createdAt)
               FROM bidding_log bl
               WHERE bl.auction.id = b.auction.id
            )
            order by auctionCreatedAt
    """)
    List<BidInfoProjection> getBiddingLogInfoList();
}
