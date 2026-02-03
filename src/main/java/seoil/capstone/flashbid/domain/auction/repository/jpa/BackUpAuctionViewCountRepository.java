package seoil.capstone.flashbid.domain.auction.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import seoil.capstone.flashbid.domain.auction.entity.AuctionViewCountEntity;

public interface BackUpAuctionViewCountRepository extends JpaRepository<AuctionViewCountEntity, Long> {
    @Query("""
            update AuctionViewCount
            set viewCount=:viewCount
            where id=:auctionId
            and viewCount>:viewCount
            """)
    int updateViewCountAuctionId(Long auctionId,Long viewCount);
}
