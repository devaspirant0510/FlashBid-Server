package seoil.capstone.flashbid.domain.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import seoil.capstone.flashbid.domain.notification.entity.NotificationEntity;
import seoil.capstone.flashbid.domain.notification.entity.NotificationReadEntity;
import seoil.capstone.flashbid.domain.notification.repository.NotificationReadRepository;
import seoil.capstone.flashbid.domain.notification.repository.NotificationRepository;
import seoil.capstone.flashbid.domain.user.repository.AccountRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final NotificationReadRepository notificationReadRepository;
    private final AccountRepository accountRepository;

    // 사용자 알림 페이징 조회
    public Page<NotificationEntity> getMyNotification(Long userId, int page) {
        return notificationRepository.findByAccountIdOrAccountIsNullOrderByCreatedAtDesc(userId, PageRequest.of(page, 10));
    }

    // 읽지 않은 알림 개수
    public long countUnreadNotifications(Long userId) {
        return notificationRepository.countUnreadNotifications(userId);
    }

    // 알림 읽음 처리
    public void readMyNotification(Long userId) {
        notificationReadRepository.findByAccountId(userId).ifPresentOrElse(
                notiRead -> {
                    notiRead.setReadAt(LocalDateTime.now());
                    notificationReadRepository.save(notiRead);
                },
                () -> {
                    NotificationReadEntity newNotiRead = NotificationReadEntity.builder()
                            .account(accountRepository.findById(userId)
                                    .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다")))
                            .readAt(LocalDateTime.now())
                            .build();
                    notificationReadRepository.save(newNotiRead);
                }
        );
    }
}