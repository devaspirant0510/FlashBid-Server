package seoil.capstone.flashbid.domain.feed.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import seoil.capstone.flashbid.domain.feed.entity.FeedAuctionEntity;

public interface FeedAuctionRepository extends JpaRepository<FeedAuctionEntity,Long> {
}
