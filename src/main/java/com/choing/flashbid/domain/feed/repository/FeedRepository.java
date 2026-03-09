package com.choing.flashbid.domain.feed.repository;


import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.choing.flashbid.domain.feed.entity.FeedEntity;
import com.choing.flashbid.domain.feed.projection.FeedAuctionProjection;
import com.choing.flashbid.domain.feed.projection.FeedProjection;
import com.choing.flashbid.domain.feed.projection.FeedSummaryProjection;

import java.util.List;

public interface FeedRepository extends JpaRepository<FeedEntity,Long> {
    int countByUserId(Long userId);
    List<FeedEntity> findAllByUserId(Long userId);
    List<FeedEntity> findTop4ByOrderByCreatedAtDesc();

    @Query("""
                SELECT
                    f.id AS id,
                    f.contents AS contents,
                    u.id AS writerId,
                    u.nickname AS writerName,
                    u.profileUrl AS writerProfileImageUrl,
                    f.createdAt AS createdAt,
                    (SELECT COUNT(l.id) FROM likes l WHERE l.feed.id = f.id) AS likeCount,
                    (SELECT COUNT(c.id) FROM CommentEntity c WHERE c.feed.id = f.id) AS commentCount,
                    CASE
                        WHEN :userId IS NULL THEN false
                        WHEN EXISTS (
                            SELECT 1
                            FROM likes l2
                            WHERE l2.feed.id = f.id AND l2.account.id = :userId
                        )
                        THEN true
                        ELSE false
                    END AS liked
                FROM feed f
                JOIN f.user u
                ORDER BY f.createdAt DESC
            """)
    Slice<FeedProjection> findAllFeedQuery(Pageable pageable,Long userId);

    @Query("""
            SELECT
                f.id AS id,
                f.contents AS contents,
                u.id AS writerId,
                u.nickname AS writerName,
                u.profileUrl AS writerProfileImageUrl,
                f.createdAt AS createdAt,
            
                COUNT(DISTINCT l.id) AS likeCount,
                COUNT(DISTINCT c.id) AS commentCount,
            
                CASE
                    WHEN :userId IS NULL THEN false
                    WHEN ul.id IS NOT NULL THEN true
                    ELSE false
                END AS liked,
                a.id as auctionId,
                g.title as auctionTitle,
                g.description as auctionDescription,
                awl.count as auctionLikeCount,
                avc.viewCount as auctionViewCount,
                a.auctionType as auctionType,
                a.startPrice as auctionStartPrice,
                a.auctionStatus as auctionStatus,
                a.startTime as auctionStartTime,
                a.endTime as auctionEndTime,
                ast.lastBidAmount as auctionCurrentPrice,
                ca.name as auctionCategoryName,
                (
                    select ff.url
                    from file ff
                    where ff.fileId=a.id
                    and ff.fileType=com.choing.flashbid.global.common.enums.FileType.GOODS
                                order by ff.id asc
                    limit 1) as auctionImageUrl
            FROM feed f
            JOIN f.user u
            LEFT JOIN FeedAuction fa ON fa.feed.id = f.id
            LEFT JOIN Auction a ON fa.auction.id = a.id
            LEFT JOIN AuctionViewCount avc ON avc.id = a.id
            LEFT JOIN category ca on a.category.id = ca.id
            LEFT JOIN AuctionWishlistCount awl ON awl.auction.id = a.id
            LEFT JOIN AuctionStats ast ON a.id = ast.auction.id
            LEFT JOIN Goods g ON a.goods.id = g.id
            LEFT JOIN likes l ON l.feed.id = f.id
            LEFT JOIN CommentEntity c ON c.feed.id = f.id
            LEFT JOIN likes ul ON ul.feed.id = f.id AND ul.account.id = :userId
            GROUP BY
                a.id,f.id, u.id, g.id, ul.id,awl.id,ast.id,ca.id
            ORDER BY f.createdAt DESC
            """)
    Slice<FeedSummaryProjection> findAllFeedQueryV2(Pageable pageable, Long userId);

    @Query("""
            SELECT
                f.id AS id,
                f.contents AS contents,
                u.id AS writerId,
                u.nickname AS writerName,
                u.profileUrl AS writerProfileImageUrl,
                f.createdAt AS createdAt,
            
                COUNT(DISTINCT l.id) AS likeCount,
                COUNT(DISTINCT c.id) AS commentCount,
            
                CASE
                    WHEN :userId IS NULL THEN false
                    WHEN ul.id IS NOT NULL THEN true
                    ELSE false
                END AS liked,
                a.id as auctionId,
                g.title as auctionTitle,
                g.description as auctionDescription,
                awl.count as auctionLikeCount,
                avc.viewCount as auctionViewCount,
                a.auctionType as auctionType,
                a.startPrice as auctionStartPrice,
                a.auctionStatus as auctionStatus,
                a.startTime as auctionStartTime,
                a.endTime as auctionEndTime,
                ast.lastBidAmount as auctionCurrentPrice,
                ca.name as auctionCategoryName,
                (
                    select ff.url
                    from file ff
                    where ff.fileId=a.id
                    and ff.fileType=com.choing.flashbid.global.common.enums.FileType.GOODS
                                order by ff.id asc
                    limit 1) as auctionImageUrl,
                    cb.id AS confirmBidId,
                    cba.id AS confirmBidAuctionId,
                    cba.auctionType AS confirmBidAuctionType,
                    cbg.title AS confirmBidAuctionTitle,
                    cbg.description AS confirmBidAuctionDescription,
                    cbca.name AS confirmBidAuctionCategoryName,
                    cba.startPrice AS confirmBidAuctionStartPrice,
                    cba.startTime AS confirmBidAuctionStartTime,
                    cba.endTime AS confirmBidAuctionEndTime,
                    cbl.price AS confirmedBidPrice,
                    cbb.nickname AS confirmedBidBidderName,
                    cbb.profileUrl AS confirmedBidBidderProfileImage,
                    COUNT(DISTINCT cbl2.id) AS confirmedBidBiddingCount,
                    COUNT(DISTINCT chat.id) AS confirmedBidChatCount
            FROM feed f
            JOIN f.user u
            LEFT JOIN FeedAuction fa ON fa.feed.id = f.id
            LEFT JOIN FeedConfirmBid fcf ON fcf.feed.id = f.id
            LEFT JOIN AuctionViewCount avc on avc.id = fa.auction.id
            LEFT JOIN fcf.confirmedBids cb
            LEFT JOIN cb.auction cba
            LEFT JOIN cba.goods cbg
            LEFT JOIN cba.category cbca
            LEFT JOIN cb.biddingLog cbl
            LEFT JOIN cb.bidder cbb
            LEFT JOIN bidding_log cbl2 ON cbl2.auction.id = cba.id
            LEFT JOIN AuctionChat chat ON chat.auction.id = cba.id
            LEFT JOIN Auction a ON fa.auction.id = a.id
            LEFT JOIN a.category ca
            LEFT JOIN AuctionWishlistCount awl ON awl.auction.id = a.id
            LEFT JOIN AuctionStats ast ON a.id = ast.auction.id
            LEFT JOIN Goods g ON a.goods.id = g.id
            LEFT JOIN likes l ON l.feed.id = f.id
            LEFT JOIN CommentEntity c ON c.feed.id = f.id
            LEFT JOIN likes ul ON ul.feed.id = f.id AND ul.account.id = :userId
            WHERE (:cursorId IS NULL OR f.id < :cursorId)
            GROUP BY
                a.id,f.id, u.id, g.id, ul.id,awl.id,ast.id,ca.id,cb.id,cba.id,cbg.id,cbca.id,cbl.id,cbb.id,avc.id
            ORDER BY f.createdAt DESC
            """)
    List<FeedSummaryProjection> findAllFeedQueryCursor(
            @Param("cursorId") Long cursorId,
            @Param("userId") Long userId,
            Pageable pageable
    );

    @Query("""
                select
                f.id
                from feed f
                join Account u
                on f.user.id = u.id
                
            """)
    List<FeedProjection> findAllFeedWithCursor(Long userId, Long pageSize,Long cursorId);

    @Query("""
            select
                au.id as auctionId,
                au.goods.title as auctionTitle,
                au.goods.description as auctionDescription,
                au.category.name as categoryName,
                (
                    SELECT f.url
                    FROM file f
                    WHERE f.fileId = au.id
                    AND f.fileType = com.choing.flashbid.global.common.enums.FileType.GOODS
                    ORDER BY f.id ASC
                    LIMIT 1
                ) AS thumbnail,
                au.startPrice as startPrice,
                (
                    SELECT b.price
                    FROM bidding_log b
                    WHERE b.auction.id = au.id
                    ORDER BY b.createdAt DESC
                    LIMIT 1
                ) AS currentPrice,
                au.startTime as startTime,
                au.endTime as endTime
            from Account ac
            join Auction au
            on ac.id = au.user.id
            where au.auctionStatus != com.choing.flashbid.global.common.enums.AuctionStatus.ENDED
            and au.user.id =:accountId
            """)
    List<FeedAuctionProjection> findMyFeedPostedAuction(Long accountId);

}
