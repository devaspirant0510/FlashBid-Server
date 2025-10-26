package seoil.capstone.flashbid.domain.auction.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import seoil.capstone.flashbid.domain.auction.entity.Auction;
import seoil.capstone.flashbid.domain.auction.projection.AuctionProjection;
import seoil.capstone.flashbid.global.common.enums.AuctionType;

import java.time.LocalDateTime;
import java.util.List;

public interface AuctionRepository extends JpaRepository<Auction, Long> {
    List<Auction> findAllByOrderByCreatedAtDesc();

    List<Auction> findAllByAuctionTypeAndEndTimeAfterOrderByCreatedAtDesc(AuctionType auctionType, LocalDateTime now);

    List<Auction> findTop4ByOrderByCreatedAtDesc();

    List<Auction> findAllByIdNot(Long id);

    @Query("""
                SELECT
                    a.id AS id,
                    g.title AS goodsTitle,
                    (SELECT f.url
                     FROM file f
                     WHERE f.fileId = g.id
                       AND f.fileType = 'GOODS'
                     ORDER BY f.id ASC
                     LIMIT 1) AS goodsImageUrl,
                    acc.nickname AS bidderName,
                    c.name AS categoryName,
                    a.startPrice AS startPrice,
                    a.viewCount AS viewCount,
                    a.startTime AS startTime,
                    a.endTime AS endTime,
                    a.auctionStatus AS status,
                    COALESCE(w.count, 0) AS likeCount,
                    COALESCE(( SELECT COUNT(p.id) FROM auction_participate p WHERE p.auction.id = a.id),0) AS participateCount,
                    (select count(b.id) from bidding_log b where b.auction.id=a.id) as biddingCount,
                    (select count(ac.id) from AuctionChat ac where ac.auction.id=a.id) as chatMessagingCount,
                    (SELECT MAX(b.price) FROM bidding_log b WHERE b.auction.id = a.id) AS currentPrice
                FROM Auction a
                    JOIN a.goods g
                    JOIN a.user acc
                    JOIN a.category c
                    LEFT JOIN AuctionWishlistCount w ON w.auction.id = a.id
                WHERE a.auctionType = :auctionType
                  AND c.root IS NULL
                  AND a.endTime > CURRENT_TIMESTAMP
                ORDER BY a.createdAt DESC
            """)
    Slice<AuctionProjection> findAllByLiveAuctionPage(AuctionType auctionType, Pageable pageable);
}
