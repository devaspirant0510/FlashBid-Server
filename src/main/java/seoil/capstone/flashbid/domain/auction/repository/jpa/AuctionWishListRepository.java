package seoil.capstone.flashbid.domain.auction.repository.jpa;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import seoil.capstone.flashbid.domain.auction.entity.AuctionWishListEntity;

import java.util.List;
import java.util.Optional;

public interface AuctionWishListRepository extends JpaRepository<AuctionWishListEntity,Long> {
    Optional<AuctionWishListEntity> findByUserIdAndAuctionId(Long userId, Long auctionId);
    boolean existsByUserIdAndAuctionId(Long userId, Long auctionId);
    void deleteByUserIdAndAuctionId(Long userId, Long auctionId);

    @Query("SELECT awl FROM AuctionWishList awl JOIN FETCH awl.auction WHERE awl.user.id = :userId")
    List<AuctionWishListEntity> findAllByUserId(@Param("userId") Long userId);
}
