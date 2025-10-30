package seoil.capstone.flashbid.domain.auction.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import seoil.capstone.flashbid.domain.auction.entity.AuctionParticipateEntity;
import seoil.capstone.flashbid.domain.auction.projection.AuctionParticipantsProjection;

public interface AuctionParticipateRepository extends JpaRepository<AuctionParticipateEntity, Long> {
    int countByAuctionId(Long auctionId);

    boolean existsByAuctionIdAndParticipantId(Long auctionId, Long participateId);

    @Query(value = """
                select ap.id as id, ap.participant.id as participantId, a.nickname as nickname, a.profileUrl as profileUrl
                from auction_participate ap
                join Account a
                on a.id = ap.participant.id
                where ap.auction.id = :auctionId
            """,
            countQuery = """
                    select count(ap)
                    from auction_participate ap
                    where ap.auction.id = :auctionId
                    """

    )
    Page<AuctionParticipantsProjection> findAllByAuctionId(@Param("auctionId") Long auctionId, Pageable pageable);
}
