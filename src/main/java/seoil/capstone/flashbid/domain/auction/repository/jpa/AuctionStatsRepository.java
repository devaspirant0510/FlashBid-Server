package seoil.capstone.flashbid.domain.auction.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import seoil.capstone.flashbid.domain.auction.entity.AuctionStatsEntity;

public interface AuctionStatsRepository extends JpaRepository<AuctionStatsEntity, Long> {
}
