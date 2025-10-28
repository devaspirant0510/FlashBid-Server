package seoil.capstone.flashbid.domain.auction.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import seoil.capstone.flashbid.domain.auction.entity.Auction;
import seoil.capstone.flashbid.domain.auction.entity.BiddingLogEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface BiddingLogRepository extends JpaRepository<BiddingLogEntity, Long> {
    // 특정 경매의 최신 입찰 조회
    Optional<BiddingLogEntity> findTopByAuctionOrderByIdDesc(Auction auction);

    // 특정 경매의 모든 입찰 조회
    List<BiddingLogEntity> findByAuction(Auction auction);
}