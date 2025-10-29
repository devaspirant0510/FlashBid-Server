package seoil.capstone.flashbid.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import seoil.capstone.flashbid.domain.user.entity.Account;
import seoil.capstone.flashbid.domain.user.projection.AccountStatusInfoProjection;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByUuid(String uuid);
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
    Optional<Account> findByEmail(String email);
    @Query("""
            select 
            (select count(*) from FollowEntity f where f.follower.id = a.id) as followerCount,
            (select count(*) from FollowEntity f where f.following.id = a.id) as followingCount,
            (select count(*) from confirm_bids  b where b.bidder.id = a.id) as biddingCount,
            (select count(*) from confirm_bids  b where b.seller.id = a.id) as sellCount,
            a.nickname as nickname,
            a.profileUrl as profileUrl
            from Account a
            where a.id = :id 
    """)
    AccountStatusInfoProjection findAccountStatusInfoById(Long id);
}
