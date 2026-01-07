package seoil.capstone.flashbid.domain.admin.repository.jpa;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import seoil.capstone.flashbid.domain.admin.projection.AccountDashboardProjection;
import seoil.capstone.flashbid.domain.admin.repository.querydsl.AccountAdminQueryRepository;
import seoil.capstone.flashbid.domain.user.entity.Account;
import seoil.capstone.flashbid.global.common.enums.UserStatus;

import java.time.LocalDateTime;


public interface AccountAdminRepository extends JpaRepository<Account, Long>, AccountAdminQueryRepository {

    <T> Page<T> findAllBy(Pageable page, Class<T> type);
    <T> Page<T> findAllByUserStatus(UserStatus userStatus,Pageable page,Class<T> type);
    @Query(value = """
                SELECT
                    COUNT(a.id)                                                   AS totalUserCount,
                    COUNT(a.id) FILTER (
                        WHERE a.created_at >= :todayStart AND a.created_at < :tomorrowStart
                    )                                                             AS todayUserCount,
                    COUNT(a.id) FILTER (
                        WHERE a.created_at >= :yesterdayStart AND a.created_at < :todayStart
                    )                                                             AS yesterdayUserCount
                FROM account a
            """, nativeQuery = true)
    AccountDashboardProjection getAccountDashboardStats(
            LocalDateTime todayStart,
            LocalDateTime tomorrowStart,
            LocalDateTime yesterdayStart
    );
}
