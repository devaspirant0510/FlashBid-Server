package seoil.capstone.flashbid.domain.auction.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import seoil.capstone.flashbid.domain.auction.entity.AuctionChatEntity;

import java.util.List;

public interface AuctionChatRepository extends JpaRepository<AuctionChatEntity,Long> {
    List<AuctionChatEntity> findAllByAuctionId(Long auctionId);
    Long countByAuctionId(Long auctionId);
}
