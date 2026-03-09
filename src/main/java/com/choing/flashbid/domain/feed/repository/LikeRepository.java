package com.choing.flashbid.domain.feed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.choing.flashbid.domain.feed.entity.LikeEntity;

public interface LikeRepository extends JpaRepository<LikeEntity,Long> {
    int deleteByFeedIdAndAccountId(Long feedId,Long postId);
    int countByFeedId(Long feedId);
    boolean existsByFeedIdAndAccountId(Long feedId, Long userId);
    void deleteByFeedId(Long feedId);
}
