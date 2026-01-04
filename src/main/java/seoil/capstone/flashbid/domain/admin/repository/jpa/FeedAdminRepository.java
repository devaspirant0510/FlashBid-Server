package seoil.capstone.flashbid.domain.admin.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import seoil.capstone.flashbid.domain.feed.entity.FeedEntity;

public interface FeedAdminRepository extends JpaRepository<FeedEntity,Long> {
}
