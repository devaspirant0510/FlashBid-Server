package com.choing.flashbid.domain.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.choing.flashbid.domain.notification.dto.NotificationResponseDto;
import com.choing.flashbid.domain.notification.entity.NotificationEntity;
import com.choing.flashbid.domain.notification.entity.NotificationReadEntity;
import com.choing.flashbid.domain.notification.repository.NotificationReadRepository;
import com.choing.flashbid.domain.notification.repository.NotificationRepository;
import com.choing.flashbid.domain.user.repository.AccountRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final NotificationReadRepository notificationReadRepository;
    private final AccountRepository accountRepository;

    // 사용자의 모든 알림 페이징 조회
    public Page<NotificationResponseDto> getMyNotifications(Long userId, int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<NotificationEntity> notifications = notificationRepository.findByAccountIdOrAccountIsNullOrderByCreatedAtDesc(
                userId,
                pageable
        );

        return notifications.map(notification -> {
            boolean isRead = notificationReadRepository
                    .findByAccountIdAndNotificationId(userId, notification.getId())
                    .isPresent();
            return NotificationResponseDto.from(notification, isRead);
        });
    }

    // 최근 5개 알림 조회 (팝업용)
    public Page<NotificationResponseDto> getRecentNotifications(Long userId) {
        Pageable pageable = PageRequest.of(0, 5);
        Page<NotificationEntity> notifications = notificationRepository.findByAccountIdOrAccountIsNullOrderByCreatedAtDesc(
                userId,
                pageable
        );

        return notifications.map(notification -> {
            boolean isRead = notificationReadRepository
                    .findByAccountIdAndNotificationId(userId, notification.getId())
                    .isPresent();
            return NotificationResponseDto.from(notification, isRead);
        });
    }

    // 읽지 않은 알림 개수
    public long countUnreadNotifications(Long userId) {
        return notificationRepository.countUnreadNotifications(userId);
    }

    // 팝업의 최근 5개 알림을 읽음으로 표시
    @Transactional
    public void markAllNotificationsAsRead(Long userId) {
        var account = accountRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다"));

        Page<NotificationEntity> notifications = notificationRepository.findByAccountIdOrAccountIsNullOrderByCreatedAtDesc(
                userId,
                PageRequest.of(0, 5)
        );

        for (NotificationEntity notification : notifications) {
            var existingRead = notificationReadRepository.findByAccountIdAndNotificationId(userId, notification.getId());

            if (existingRead.isEmpty()) {
                NotificationReadEntity newRead = NotificationReadEntity.builder()
                        .account(account)
                        .notification(notification)
                        .readAt(LocalDateTime.now())
                        .build();
                notificationReadRepository.save(newRead);
            }
        }

        log.info("사용자 {}의 최근 알림 5개가 읽음으로 표시되었습니다", userId);
    }

    // 모든 안 읽은 알림을 읽음으로 표시 (알림 목록 페이지 진입 시)
    @Transactional
    public void markAllUnreadNotificationsAsRead(Long userId) {
        var account = accountRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다"));

        // 사용자가 볼 수 있는 모든 알림 조회 (페이징 없이 전체)
        int page = 0;
        int pageSize = 100; // 한 번에 100개씩 처리
        Page<NotificationEntity> notificationsPage;

        do {
            Pageable pageable = PageRequest.of(page, pageSize);
            notificationsPage = notificationRepository.findByAccountIdOrAccountIsNullOrderByCreatedAtDesc(
                    userId,
                    pageable
            );

            // 각 알림에 대해 읽음 처리
            for (NotificationEntity notification : notificationsPage.getContent()) {
                var existingRead = notificationReadRepository.findByAccountIdAndNotificationId(userId, notification.getId());

                // 아직 읽지 않은 알림만 처리
                if (existingRead.isEmpty()) {
                    NotificationReadEntity newRead = NotificationReadEntity.builder()
                            .account(account)
                            .notification(notification)
                            .readAt(LocalDateTime.now())
                            .build();
                    notificationReadRepository.save(newRead);
                }
            }

            page++;
        } while (notificationsPage.hasNext()); // 다음 페이지가 있으면 계속

        log.info("사용자 {}의 모든 안 읽은 알림이 읽음으로 표시되었습니다", userId);
    }
}