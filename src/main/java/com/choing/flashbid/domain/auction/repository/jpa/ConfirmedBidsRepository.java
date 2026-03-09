package com.choing.flashbid.domain.auction.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.choing.flashbid.domain.auction.entity.ConfirmedBidsEntity;
import com.choing.flashbid.domain.feed.projection.FeedConfirmBidsProjection;

import java.util.List;
import java.util.Optional;

public interface ConfirmedBidsRepository extends JpaRepository<ConfirmedBidsEntity, Long> {
    List<ConfirmedBidsEntity> findAllBySeller_Id(Long sellerId);

    List<ConfirmedBidsEntity> findAllByBidder_Id(Long bidderId);

    Optional<ConfirmedBidsEntity> findByAuctionId(Long auctionId);

    @Query("""
            SELECT
                cb.id AS confirmBidId,
                a.id AS auctionId,
                a.auctionType as auctionType,
                g.title AS auctionTitle,
                g.description AS auctionDescription,
                c.name AS auctionCategoryName,
                a.startPrice AS auctionStartPrice,
                a.startTime AS auctionStartTime,
                a.endTime AS auctionEndTime,
                bl.price AS bidConfirmedPrice,
                ac.nickname AS bidderName,
                ac.profileUrl AS bidderProfileImage,
                ast.biddingCount as biddingCount,
                acc.chatCount as chatCount
            FROM confirm_bids cb
                JOIN cb.auction a
                JOIN cb.biddingLog bl
                JOIN a.goods g
                JOIN a.category c
                JOIN cb.bidder ac
                LEFT JOIN AuctionStats ast ON ast.auction.id = a.id
                LEFT JOIN AuctionChatCount  acc ON acc.id = a.id
            WHERE ac.id = :userId
            """)
    List<FeedConfirmBidsProjection> findAllMyConfirmedBids(@Param("userId") Long userId);
}
