package seoil.capstone.flashbid.domain.auction.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import seoil.capstone.flashbid.domain.auction.entity.BiddingLogEntity;

public interface AuctionBidLogRepository extends JpaRepository<BiddingLogEntity,Long> {
    BiddingLogEntity findTop1ByAuctionIdOrderByCreatedAtDesc(Long auctionId);
    Long countByAuctionId(Long auctionId);
}
