package seoil.capstone.flashbid.domain.feed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import seoil.capstone.flashbid.domain.feed.entity.LikeEntity;

public interface LikeRepository extends JpaRepository<LikeEntity,Long> {
    int deleteByFeedIdAndAccountId(Long feedId,Long postId);
    int countByFeedId(Long feedId);

}
