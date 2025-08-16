package seoil.capstone.flashbid.domain.auction.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import seoil.capstone.flashbid.domain.auction.entity.AuctionWishListEntity;

import java.util.Optional;

public interface AuctionWishListRepository extends JpaRepository<AuctionWishListEntity,Long> {
    Optional<AuctionWishListEntity> findByUserIdAndAuctionId(Long userId, Long auctionId);
    boolean existsByUserIdAndAuctionId(Long userId, Long auctionId);
    void deleteByUserIdAndAuctionId(Long userId, Long auctionId);
}
