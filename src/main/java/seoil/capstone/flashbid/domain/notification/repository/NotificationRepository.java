package seoil.capstone.flashbid.domain.notification.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import seoil.capstone.flashbid.domain.notification.entity.NotificationEntity;

import java.time.LocalDateTime;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
    // 사용자의 모든 알림 조회 (자신의 알림 + 전체 공지)
    Page<NotificationEntity> findByAccountIdOrAccountIsNullOrderByCreatedAtDesc(Long accountId, Pageable pageable);

    // 읽지 않은 알림 개수 계산
    // readAt이 알림의 createdAt보다 이전이거나 없으면 읽지 않은 것 -> nr.id IS NULL로 수정
    @Query("""
    SELECT COUNT(n.id)
    FROM Notification n
    LEFT JOIN NotificationRead nr ON n.id = nr.notification.id AND nr.account.id = :userId
    WHERE (n.account.id = :userId OR n.account IS NULL)
    AND nr.id IS NULL
    """)
    long countUnreadNotifications(@Param("userId") Long userId);
}