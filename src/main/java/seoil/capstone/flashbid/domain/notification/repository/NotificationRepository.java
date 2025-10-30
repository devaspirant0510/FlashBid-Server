package seoil.capstone.flashbid.domain.notification.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import seoil.capstone.flashbid.domain.notification.entity.NotificationEntity;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
    Page<NotificationEntity> findByAccountIdOrAccountIsNullOrderByCreatedAtDesc(Long accountId, Pageable pageable);
    @Query("""
    select count(n.id)
    from Notification n
    where n.account.id = :userId or n.account.id is null
    """)
    long countUnreadNotifications(Long userId);
}
