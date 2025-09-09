package seoil.capstone.flashbid.domain.auction.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import seoil.capstone.flashbid.domain.auction.entity.BiddingLogEntity;
import seoil.capstone.flashbid.domain.auction.projection.BidLoggingChartProjection;
import seoil.capstone.flashbid.domain.auction.projection.BidLoggingProjection;

import java.util.List;

public interface AuctionBidLogRepository extends JpaRepository<BiddingLogEntity, Long> {
    BiddingLogEntity findTop1ByAuctionIdOrderByCreatedAtDesc(Long auctionId);
    Long countByAuctionId(Long auctionId);

    @Query("""
                SELECT b.id AS id, 
                       b.createdAt AS createdAt, 
                       b.prevPrice AS prevPrice, 
                       b.price AS price, 
                       a.id AS bidderId, 
                       a.nickname AS bidderName, 
                       a.profileUrl AS profileUrl,
                       au.id AS auctionId
                FROM bidding_log b
                JOIN b.bidder a
                JOIN b.auction au
                WHERE b.auction.id = :auctionId
                ORDER BY b.createdAt DESC
            """)
    List<BidLoggingProjection> findAllBidLogHistoryByAuctionId(@Param("auctionId") Long auctionId, Pageable pageable);

    @Query(value = """
                SELECT b.id AS id, 
                       b.createdAt AS createdAt, 
                       b.prevPrice AS prevPrice, 
                       b.price AS price, 
                       a.id AS bidderId, 
                       a.nickname AS bidderName, 
                       a.profileUrl AS profileUrl,
                       au.id AS auctionId
                FROM bidding_log b
                JOIN b.bidder a
                JOIN b.auction au
                WHERE b.auction.id = :auctionId
                ORDER BY b.createdAt DESC
            """,
    countQuery = "select count(b.id) from bidding_log b where b.auction.id =:auctionId ")
    Page<List<BidLoggingProjection>> findAllBidLogHistoryByAuctionIdWithPage(@Param("auctionId") Long auctionId, Pageable pageable);

    List<BidLoggingChartProjection> findAllByAuctionIdOrderByCreatedAtAsc(Long auctionId);
}
