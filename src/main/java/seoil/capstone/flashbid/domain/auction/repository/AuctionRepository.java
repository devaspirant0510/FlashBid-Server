package seoil.capstone.flashbid.domain.auction.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import seoil.capstone.flashbid.domain.auction.entity.Auction;
import seoil.capstone.flashbid.global.common.enums.AuctionType;

import java.util.List;

public interface AuctionRepository extends JpaRepository<Auction,Long> {
    List<Auction> findAllByOrderByCreatedAtDesc();
    List<Auction> findAllByAuctionTypeOrderByCreatedAtDesc(AuctionType auctionType);
    List<Auction> findTop4ByOrderByCreatedAtDesc();
    List<Auction> findAllByIdNot(Long id);


}
