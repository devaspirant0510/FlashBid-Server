package com.choing.flashbid.domain.admin.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.choing.flashbid.domain.admin.projection.BidInfoProjection;
import com.choing.flashbid.domain.admin.projection.BiddingDashboardProjection;
import com.choing.flashbid.domain.auction.entity.BiddingLogEntity;

import java.time.LocalDateTime;
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

    @Query(value = """
                SELECT
                    COALESCE(SUM(bl.price), 0)                                           AS totalSales,
                    COALESCE(SUM(bl.price) FILTER (
                        WHERE bl.created_at >= :todayStart AND bl.created_at < :tomorrowStart
                    ), 0)                                                             AS todaySales,
                    COALESCE(SUM(bl.price) FILTER (
                        WHERE bl.created_at >= :yesterdayStart AND bl.created_at < :todayStart
                    ), 0)                                                             AS yesterdaySales,
            
                    COUNT(cb.id)                                                      AS totalConfirmedCount,
                    COUNT(cb.id) FILTER (
                        WHERE bl.created_at >= :todayStart AND bl.created_at < :tomorrowStart
                    )                                                                 AS todayConfirmedCount,
                    COUNT(cb.id) FILTER (
                        WHERE bl.created_at >= :yesterdayStart AND bl.created_at < :todayStart
                    )                                                                 AS yesterdayConfirmedCount
                FROM confirm_bids cb
                JOIN bidding_log bl ON cb.bidding_log_id = bl.id
                JOIN auction a on a.id = cb.auction_id
                where a.auction_type= :auctionType
            """, nativeQuery = true)
    BiddingDashboardProjection getDashboardStats(
            LocalDateTime todayStart,
            LocalDateTime tomorrowStart,
            LocalDateTime yesterdayStart,
            Integer auctionType
    );
}
