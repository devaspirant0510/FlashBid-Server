package seoil.capstone.flashbid.domain.auction.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import seoil.capstone.flashbid.domain.auction.entity.ConfirmedBidsEntity;

import java.util.List;
import java.util.Optional;

public interface ConfirmedBidsRepository extends JpaRepository<ConfirmedBidsEntity,Long> {
    List<ConfirmedBidsEntity> findAllBySeller_Id(Long sellerId);
    List<ConfirmedBidsEntity> findAllByBidder_Id(Long bidderId);
    Optional<ConfirmedBidsEntity> findByAuctionId(Long auctionId);
}
