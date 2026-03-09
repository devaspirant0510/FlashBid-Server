package com.choing.flashbid.domain.auction.repository.jpa;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.choing.flashbid.domain.auction.entity.Auction;
import com.choing.flashbid.domain.auction.entity.AuctionWishListCountEntity;

import java.util.Optional;

public interface AuctionWishListCountRepository extends JpaRepository<AuctionWishListCountEntity, Long> {
    AuctionWishListCountEntity findByAuctionId(Long auctionId);

    @Modifying
    @Query("UPDATE AuctionWishlistCount c SET c.count = c.count + 1 WHERE c.auction.id = :auctionId")
    int incrementCountByAuctionId(Long auctionId);

    @Modifying
    @Query("UPDATE AuctionWishlistCount c SET c.count = c.count - 1 WHERE c.auction.id = :auctionId")
    int decrementCountByAuctionId(Long auctionId);

    @Modifying
    @Query(
            value = """
            INSERT INTO auction_wishlist_count (id, count)
            VALUES (:auctionId, 1)
            ON CONFLICT (id)
            DO UPDATE SET count = auction_wishlist_count.count + 1
            """,
            nativeQuery = true
    )
    void upsertWishListCount(@Param("auctionId") Long auctionId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            SELECT ac
            FROM AuctionWishlistCount ac
            WHERE ac.id = :auctionId
            """)
    Optional<AuctionWishListCountEntity> findByAuctionIdWithPessimisticLock(Long auctionId);

    @Query("""
            SELECT ac.count FROM AuctionWishlistCount ac WHERE ac.id = :auctionId
            """)
    Optional<Long> countWishList(Long auctionId);

    Long auction(Auction auction);
}
