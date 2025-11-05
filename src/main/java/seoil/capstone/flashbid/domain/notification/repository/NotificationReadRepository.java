package seoil.capstone.flashbid.domain.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import seoil.capstone.flashbid.domain.notification.entity.NotificationReadEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface NotificationReadRepository extends JpaRepository<NotificationReadEntity, Long> {
    // Account의 id로 조회
    Optional<NotificationReadEntity> findByAccountId(Long accountId);

    // 특정 사용자가 읽은 알림 조회
    List<NotificationReadEntity> findByAccountIdAndReadAtIsNotNull(Long accountId);

    // 특정 알림이 특정 사용자에게 읽혔는지 확인
    Optional<NotificationReadEntity> findByAccountIdAndNotificationId(Long accountId, Long notificationId);

    // 특정 사용자가 읽은 알림들의 readAt 최신 시간 조회
    @Query("""
    SELECT MAX(nr.readAt)
    FROM NotificationRead nr
    WHERE nr.account.id = :accountId
    """)
    Optional<LocalDateTime> findLatestReadAtByAccountId(@Param("accountId") Long accountId);
}