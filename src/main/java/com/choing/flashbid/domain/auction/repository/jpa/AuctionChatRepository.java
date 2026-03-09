package com.choing.flashbid.domain.auction.repository.jpa;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.choing.flashbid.domain.auction.entity.AuctionChatEntity;
import com.choing.flashbid.domain.auction.projection.AuctionChatProjection;

import java.util.List;

public interface AuctionChatRepository extends JpaRepository<AuctionChatEntity, Long> {
    List<AuctionChatEntity> findAllByAuctionId(Long auctionId);

    Long countByAuctionId(Long auctionId);

    @Query("""
            select
                ac.id as id,
                ac.contents as contents,
                ac.createdAt as createdAt,
                a.id as userId,
                a.nickname as nickname,
                a.profileUrl as profileUrl,
                ac.chatType as chatType,
                bl.prevPrice as prevPrice,
                bl.price as price
            from AuctionChat ac
                join Account a
                on ac.user.id = a.id
                left join bidding_log bl
                on ac.biddingLog.id = bl.id
            where ac.auction.id = :auctionId
            order by ac.createdAt
            """)
    List<AuctionChatProjection> findAllAuctionQuery(Long auctionId);

    @Query(value = """
                select ac.id
                from auction_chat ac
                where ac.auction_id = :auctionId
                order by ac.id desc
                limit 1
            """, nativeQuery = true)
    Long getAuctionChatLastId(Long auctionId);

    @Query("""
    select
        ac.id as id,
        ac.contents as contents,
        ac.createdAt as createdAt,
        a.id as userId,
        a.nickname as nickname,
        a.profileUrl as profileUrl,
        ac.chatType as chatType,
        bl.prevPrice as prevPrice,
        bl.price as price
    from AuctionChat ac
        join Account a
        on ac.user.id = a.id
        left join bidding_log bl
        on ac.biddingLog.id = bl.id
    where ac.auction.id = :auctionId
      and (:cursorId is null or ac.id < :cursorId)
          order by ac.id desc
    """)
    Slice<AuctionChatProjection> findAllAuctionQueryWithCursor(Long auctionId, Long cursorId, Pageable pageable);}
