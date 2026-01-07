package seoil.capstone.flashbid.domain.admin.repository.jpa;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import seoil.capstone.flashbid.domain.admin.projection.AuctionAdminProjection;
import seoil.capstone.flashbid.domain.admin.projection.AuctionDashboardProjection;
import seoil.capstone.flashbid.domain.admin.projection.CategoryAuctionChartProjection;
import seoil.capstone.flashbid.domain.auction.entity.Auction;
import seoil.capstone.flashbid.global.common.enums.AuctionStatus;
import seoil.capstone.flashbid.global.common.enums.AuctionType;

import java.util.List;

public interface AuctionAdminRepository extends JpaRepository<Auction, Long> {
    @Query(value = """
            select ac.category_id ,ca.name,ac.count
                from (
                select
                category_id,
                count(category_id) as count
                from auction
                where start_time < localtimestamp
                and localtimestamp < end_time
                group by category_id
                ) ac
                join category ca
                on ca.id = ac.category_id
            """, nativeQuery = true)
    List<CategoryAuctionChartProjection> findCategoryAuctionCount();

    @Query(value = """
                SELECT
                    COUNT(*) FILTER (WHERE a.end_time > localtimestamp AND a.auction_type = :auctionType) AS activeAuctionCount,
                    COUNT(*) FILTER (WHERE a.created_at >= date_trunc('day', localtimestamp) and a.auction_type=:auctionType )  AS todayAuctionCount,
                    COUNT(*) FILTER (
                        WHERE a.auction_type = :auctionType
                          AND a.created_at >= date_trunc('day', localtimestamp) - interval '1 day'
                          AND a.created_at <  date_trunc('day', localtimestamp)
                    ) AS yesterdayAuctionCount
                FROM auction a
            """, nativeQuery = true)
    AuctionDashboardProjection getDashboardStats(@Param("auctionType") Integer auctionType);

    @Query(
            value = """
                    SELECT 
                        a.id AS auctionId,
                        g.title AS goodsTitle,
                        u.nickname AS sellerNickname,
                        c.name AS categoryName,
                    
                        a.start_price AS startPrice,
                        COALESCE(ast.last_bid_amount, 0) AS lastBidAmount,
                    
                        COALESCE(ast.bidding_count, 0) AS biddingCount,
                        COALESCE(ast.participants_count, 0) AS participantsCount,
                        COALESCE(ast.chat_count, 0) AS chatCount,
                    
                        a.view_count AS viewCount,
                    
                        a.auction_type AS auctionType,
                        a.auction_status AS auctionStatus,
                    
                        a.start_time AS startTime,
                        a.end_time AS endTime,
                        a.created_at AS createdAt
                    FROM auction a
                    JOIN (
                        SELECT id
                        FROM auction
                        WHERE (auction_type = :auctionType)
                          AND (:status IS NULL OR auction_status = :status)
                          AND (:categoryId IS NULL OR category_id = :categoryId)
                        ORDER BY id DESC
                        LIMIT :limit OFFSET :offset
                    ) t ON a.id = t.id
                    JOIN goods g ON g.id = a.goods_id
                    JOIN account u ON u.id = a.user_id
                    JOIN category c ON c.id = a.category_id
                    LEFT JOIN auction_stats ast ON ast.auction_id = a.id
                    ORDER BY a.id DESC
                    """,
            nativeQuery = true
    )
    List<AuctionAdminProjection> findAllAdminAuctions(
            @Nullable Long categoryId,
            @Nullable Integer status,
            @NonNull Integer auctionType,
            @NonNull Integer limit,
            @NonNull Integer offset
    );

    @Query(
            value = """ 
                    SELECT count(*)
                    FROM auction a
                             JOIN (SELECT id
                                   FROM auction
                                   WHERE
                                       auction_type = :auctionType
                                     AND (:categoryId IS NULL OR category_id = :categoryId)
                                     AND (:status IS NULL OR auction_status = :status)
                                     AND end_time > localtimestamp
                                   LIMIT :limit OFFSET :offset) t ON a.id = t.id;
                    """,
            nativeQuery = true
    )
    Integer countLimitOffsetByAdminAuction(
            @Nullable Long categoryId,
            @Nullable Integer status,
            @NonNull Integer auctionType,
            @NonNull Integer limit,
            @NonNull Integer offset
    );

    @Query("""
                select count(a)
                from Auction a
                where a.auctionType = :auctionType
                  and (:categoryId is null or a.category.id = :categoryId)
                  and (:status is null or a.auctionStatus = :status)
                  and a.endTime > current_timestamp
            """)
    Integer countByAdminAuction(
            @Nullable Long categoryId,
            @Nullable AuctionStatus status,
            @NonNull AuctionType auctionType
    );
}
