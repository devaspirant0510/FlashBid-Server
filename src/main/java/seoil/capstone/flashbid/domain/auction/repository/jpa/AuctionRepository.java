package seoil.capstone.flashbid.domain.auction.repository.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import seoil.capstone.flashbid.domain.auction.entity.Auction;
import seoil.capstone.flashbid.domain.auction.projection.AuctionDetailProjection;
import seoil.capstone.flashbid.domain.auction.projection.AuctionProjection;
import seoil.capstone.flashbid.global.common.enums.AuctionType;

import java.time.LocalDateTime;
import java.util.List;

public interface AuctionRepository extends JpaRepository<Auction, Long> {
    List<Auction> findAllByOrderByCreatedAtDesc();

    List<Auction> findAllByAuctionTypeAndEndTimeAfterOrderByCreatedAtDesc(AuctionType auctionType, LocalDateTime now);

    List<Auction> findTop4ByOrderByCreatedAtDesc();

    List<Auction> findAllByIdNot(Long id);

    List<Auction> findAllByUserIdOrderByCreatedAtDesc(Long userId);

    @Query(value = """
                SELECT
                    a.id AS id,
                    g.title AS goodsTitle,
                    (SELECT f.url
                     FROM file f
                     WHERE f.fileId = g.id
                       AND f.fileType = seoil.capstone.flashbid.global.common.enums.FileType.GOODS
                     ORDER BY f.id ASC
                     LIMIT 1) AS goodsImageUrl,
                    acc.nickname AS bidderName,
                    c.name AS categoryName,
                    a.startPrice AS startPrice,
                    avc.viewCount AS viewCount,
                    a.startTime AS startTime,
                    a.endTime AS endTime,
                    a.auctionStatus AS status,
                    COALESCE(w.count, 0) AS likeCount,
                    COALESCE(aus.participantsCount,0) AS participateCount,
                    COALESCE(aus.biddingCount,0) as biddingCount,
                    (select count(ac.id) from AuctionChat ac where ac.auction.id=a.id) as chatMessagingCount,
                    COALESCE(aus.lastBidAmount,0) AS currentPrice
                FROM Auction a
                    JOIN a.goods g
                    JOIN a.user acc
                    JOIN a.category c
                    LEFT JOIN AuctionWishlistCount w ON w.auction.id = a.id
                    LEFT JOIN AuctionStats aus on aus.auction.id = a.id
                    LEFT JOIN AuctionViewCount  avc on avc.id = a.id
                WHERE a.auctionType = :auctionType
                  AND c.root IS NULL
                  AND a.endTime > CURRENT_TIMESTAMP
                    AND (:categoryName IS NULL OR c.name = :categoryName)
                ORDER BY a.createdAt DESC
            """,
            countQuery = """
                                    SELECT
                                    count(*)
                                    FROM Auction a
                                        JOIN a.goods g
                                        JOIN a.user acc
                                        JOIN a.category c
                                        LEFT JOIN AuctionWishlistCount w ON w.auction.id = a.id
                                    WHERE a.auctionType = :auctionType
                                      AND c.root IS NULL
                                      AND a.endTime > CURRENT_TIMESTAMP
                                        AND (:categoryName IS NULL OR c.name = :categoryName)
                    """
    )
    Page<AuctionProjection> findAllByLiveAuctionPage(AuctionType auctionType, String categoryName, Pageable pageable);

    // TODO: scalar sub query lateral join 으로 변경
    @Query(
            value = """
                           SELECT a.id,
                           g.title AS goodsTitle,
                           u.nickname AS bidderName,
                           (
                             SELECT f.url
                             FROM file f
                             WHERE f.file_id = a.goods_id
                               AND f.file_type = 3
                             LIMIT 1
                           ) AS goodsImageUrl,
                           a.start_price AS startPrice,
                           avc.view_count AS viewCount,
                           a.start_time AS startTime,
                           a.end_time AS endTime,
                           a.auction_status AS status,
                           c.name AS categoryName,
                           COALESCE(awc.count, 0) AS likeCount,
                           COALESCE(ast.bidding_count, 0) AS biddingCount,
                           COALESCE(acc.chat_count, 0) AS chatMessagingCount,
                           COALESCE(ast.last_bid_amount, 0) AS lastBidAmount
                    FROM auction a
                    JOIN (
                        SELECT id
                        FROM auction
                        WHERE auction_type = :auctionType 
                          AND (:categoryId IS NULL OR category_id = :categoryId)
                          AND end_time > localtimestamp
                        ORDER BY id DESC
                        LIMIT :limit OFFSET :offset
                    ) t ON a.id = t.id
                    JOIN goods g ON g.id = a.goods_id
                    JOIN account u ON u.id = a.user_id
                    JOIN category c ON c.id = a.category_id
                    LEFT JOIN auction_wishlist_count awc ON awc.id = a.id
                    LEFT JOIN auction_stats ast ON ast.auction_id = a.id
                    LEFT JOIN auction_view_count avc ON avc.auction_id = a.id
                    LEFT JOIN auction_chat_count acc ON acc.auction_id = a.id
                    ORDER BY a.id DESC;
                    """,
            nativeQuery = true
    )
    List<AuctionProjection> findAllByAuctionPageV2(Long categoryId,Integer auctionType, Integer limit, Integer offset);

    @Query(
            value = """ 
                    SELECT count(*)
                    FROM auction a
                             JOIN (SELECT id
                                   FROM auction
                                   WHERE
                                       auction_type = :auctionType
                                     AND (:categoryId IS NULL OR category_id = :categoryId)
                                     AND end_time > localtimestamp
                                   LIMIT :limit OFFSET :offset) t ON a.id = t.id;
                    """,
            nativeQuery = true
    )
    Integer countByAuctionPageV2(Long categoryId,Integer auctionType, Integer limit, Integer offset);


    @Query("""
                SELECT
                    a.id AS id,
                    g.title AS title,
                    c.name AS categoryName,
                    g.description AS description,
                    COALESCE(ast.lastBidAmount, a.startPrice) AS currentPrice,
                    a.startPrice AS startPrice,
                    COALESCE(ast.participantsCount, 0) AS participateCount,
                    COALESCE(ast.biddingCount, 0) AS biddingCount,
                    COALESCE(awc.count, 0) AS likeCount,
                    COALESCE(accChat.chatCount, 0) AS chatMessagingCount,
                    a.startTime AS startTime,
                    a.endTime AS endTime,
                    a.auctionStatus AS status,
                    CASE WHEN :accountId IS NOT NULL AND EXISTS (
                        SELECT 1
                        FROM AuctionWishList aw 
                        WHERE aw.auction.id = a.id AND aw.user.id = :accountId
                    ) THEN TRUE ELSE FALSE END AS isLiked,
                    u.id AS bidderId,
                    u.nickname AS bidderNickname,
                    COALESCE(0) AS bidderFollower,
                    COALESCE(0) AS bidderFollowing,
                    COALESCE(0) AS bidderConfirmBidCount,
                    COALESCE(0) AS bidderSaleCount
                FROM Auction a
                JOIN a.goods g
                JOIN a.user u
                JOIN a.category c 
                LEFT JOIN AuctionWishlistCount awc ON awc.auction.id = a.id
                LEFT JOIN AuctionStats ast ON ast.auction.id = a.id
                LEFT JOIN AuctionChatCount accChat ON accChat.id = a.id
                WHERE a.id = :auctionId
            """)
    AuctionDetailProjection findAuctionDetailById(Long auctionId, Long accountId);


}
