package seoil.capstone.flashbid.domain.auction.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import seoil.capstone.flashbid.domain.auction.entity.BiddingLogEntity;
import seoil.capstone.flashbid.domain.auction.projection.BidLoggingChartProjection;
import seoil.capstone.flashbid.domain.auction.projection.BidLoggingProjection;
import seoil.capstone.flashbid.domain.auction.projection.UserMaxBidProjection;

import java.util.List;
import java.util.Optional;

public interface AuctionBidLogRepository extends JpaRepository<BiddingLogEntity, Long> {
    BiddingLogEntity findTop1ByAuctionIdOrderByPriceDesc(Long auctionId);
    Long countByAuctionId(Long auctionId);
    boolean existsByBidderIdAndAuctionId(Long bidderId, Long auctionId);
    Optional<BiddingLogEntity> findTop1ByBidderIdAndAuctionIdOrderByCreatedAtDesc(Long bidderId, Long auctionId);

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

    // 주어진 auctionId에 대해, 경매의 전체 최고 입찰가와 동일한 가격을 낸 bidder들의 아이디 리스트를 반환
    @Query("""
        SELECT bl.bidder.id
        FROM bidding_log bl
        WHERE bl.auction.id = :auctionId
          AND bl.price = (
            SELECT MAX(b2.price) FROM bidding_log b2 WHERE b2.auction.id = :auctionId
          )
    """)
    List<Long> findTopBiddersByAuctionId(@Param("auctionId") Long auctionId);

    // 주어진 auctionId에 대해, 각 bidder별로 최고 입찰가(MAX(price))를 구하고 bidderId와 maxPrice를 반환 (내림차순)
    @Query("SELECT bl.bidder.id AS bidderId, MAX(bl.price) AS maxPrice " +
           "FROM bidding_log bl " +
           "WHERE bl.auction.id = :auctionId " +
           "GROUP BY bl.bidder.id " +
           "ORDER BY MAX(bl.price) DESC")
    List<UserMaxBidProjection> findMaxBidPerUserByAuctionId(@Param("auctionId") Long auctionId);
}
