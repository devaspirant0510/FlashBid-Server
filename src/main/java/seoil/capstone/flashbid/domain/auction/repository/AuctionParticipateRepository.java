package seoil.capstone.flashbid.domain.auction.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import seoil.capstone.flashbid.domain.auction.entity.AuctionParticipateEntity;

public interface AuctionParticipateRepository extends JpaRepository<AuctionParticipateEntity,Long> {
    int countByAuctionId(Long auctionId);
    boolean existsByAuctionIdAndParticipantId(Long auctionId,Long participateId);
}
