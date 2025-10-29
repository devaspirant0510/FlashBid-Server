package seoil.capstone.flashbid.domain.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import seoil.capstone.flashbid.domain.notification.entity.NotificationReadEntity;

import java.util.Optional;

public interface NotificationReadRepository extends JpaRepository<NotificationReadEntity, Long> {
    // Account의 id로 조회 (userId가 아닌 accountId 사용)
    Optional<NotificationReadEntity> findByAccountId(Long accountId);
}