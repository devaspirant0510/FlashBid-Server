package seoil.capstone.flashbid.domain.auction.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import seoil.capstone.flashbid.domain.auction.entity.Auction;

public interface AuctionRepository extends JpaRepository<Auction,Long> {

}
