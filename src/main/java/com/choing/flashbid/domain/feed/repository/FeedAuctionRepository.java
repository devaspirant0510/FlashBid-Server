package com.choing.flashbid.domain.feed.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import com.choing.flashbid.domain.feed.entity.FeedAuctionEntity;

public interface FeedAuctionRepository extends JpaRepository<FeedAuctionEntity,Long> {
}
