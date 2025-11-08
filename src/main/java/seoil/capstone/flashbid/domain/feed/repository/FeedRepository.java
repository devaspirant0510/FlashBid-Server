package seoil.capstone.flashbid.domain.feed.repository;


import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import seoil.capstone.flashbid.domain.feed.entity.FeedEntity;
import seoil.capstone.flashbid.domain.feed.projection.FeedAuctionProjection;
import seoil.capstone.flashbid.domain.feed.projection.FeedProjection;

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
            select
                au.id as auctionId,
                au.goods.title as auctionTitle,
                au.goods.description as auctionDescription,
                au.category.name as categoryName,
                (
                    SELECT f.url
                    FROM file f
                    WHERE f.fileId = au.id
                    AND f.fileType = seoil.capstone.flashbid.global.common.enums.FileType.GOODS
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
            where au.auctionStatus != seoil.capstone.flashbid.global.common.enums.AuctionStatus.ENDED
            """)
    List<FeedAuctionProjection> findMyFeedPostedAuction(Long accountId);

}
