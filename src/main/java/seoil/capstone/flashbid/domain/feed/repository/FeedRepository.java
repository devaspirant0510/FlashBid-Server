package seoil.capstone.flashbid.domain.feed.repository;


import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import seoil.capstone.flashbid.domain.feed.entity.FeedEntity;
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
                u.nickname AS writerName,
                u.profileUrl AS writerProfileIMageUrl,
                f.createdAt AS createdAt,
                (SELECT COUNT(l.id) FROM likes l WHERE l.feed.id = f.id) AS likeCount,
                (SELECT COUNT(c.id) FROM CommentEntity c WHERE c.feed.id = f.id) AS commentCount
            FROM feed f
            JOIN f.user u
            on u.id = f.user.id
            ORDER BY f.createdAt DESC
    """)
    Slice<FeedProjection> findAllFeedQuery(Pageable pageable);

}
