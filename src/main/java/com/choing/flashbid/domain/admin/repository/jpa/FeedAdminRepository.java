package com.choing.flashbid.domain.admin.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import com.choing.flashbid.domain.feed.entity.FeedEntity;

public interface FeedAdminRepository extends JpaRepository<FeedEntity,Long> {
}
