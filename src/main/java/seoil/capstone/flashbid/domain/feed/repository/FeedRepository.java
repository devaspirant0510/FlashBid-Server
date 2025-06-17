package seoil.capstone.flashbid.domain.feed.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import seoil.capstone.flashbid.domain.feed.entity.FeedEntity;

import java.util.List;

public interface FeedRepository extends JpaRepository<FeedEntity,Long> {
    int countByUserId(Long userId);
    List<FeedEntity> findAllByUserId(Long userId);
    List<FeedEntity> findTop4ByOrderByCreatedAtDesc();

}
