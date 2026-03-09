package com.choing.flashbid.domain.notification.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.choing.flashbid.domain.notification.dto.NotificationResponseDto;
import com.choing.flashbid.domain.notification.service.NotificationService;
import com.choing.flashbid.domain.user.entity.Account;
import com.choing.flashbid.global.aop.annotation.AuthUser;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
@Slf4j
public class NotificationController {

    private final NotificationService notificationService;

    /**
     * 사용자의 모든 알림 조회 (페이징)
     * GET /api/v1/notifications?page=0
     */
    @GetMapping
    @AuthUser
    public ResponseEntity<List<NotificationResponseDto>> getNotifications(
            Account account,
            @RequestParam(defaultValue = "0") int page
    ) {
        Page<NotificationResponseDto> notificationsPage = notificationService.getMyNotifications(
                account.getId(),
                page
        );

        return ResponseEntity.ok(notificationsPage.getContent());
    }

    /**
     * 최근 5개 알림 조회 (팝업용)
     * GET /api/v1/notifications/recent
     */
    @GetMapping("/recent")
    @AuthUser
    public ResponseEntity<List<NotificationResponseDto>> getRecentNotifications(
            Account account
    ) {
        Page<NotificationResponseDto> notificationsPage = notificationService.getRecentNotifications(
                account.getId()
        );

        return ResponseEntity.ok(notificationsPage.getContent());
    }

    /**
     * 읽지 않은 알림 개수 조회
     * GET /api/v1/notifications/unread-count
     */
    @GetMapping("/unread-count")
    @AuthUser
    public ResponseEntity<Long> getUnreadCount(
            Account account
    ) {
        long unreadCount = notificationService.countUnreadNotifications(account.getId());
        return ResponseEntity.ok(unreadCount);
    }

    /**
     * 팝업의 최근 5개 알림을 읽음으로 표시
     * POST /api/v1/notifications/read
     */
    @PostMapping("/read")
    @AuthUser
    public ResponseEntity<Void> markAllAsRead(
            Account account
    ) {
        notificationService.markAllNotificationsAsRead(account.getId());
        return ResponseEntity.ok().build();
    }

    /**
     * 모든 안 읽은 알림을 읽음으로 표시 (알림 목록 페이지 진입 시)
     * POST /api/v1/notifications/read-all-unread
     */
    @PostMapping("/read-all-unread")
    @AuthUser
    public ResponseEntity<Void> markAllUnreadAsRead(
            Account account
    ) {
        notificationService.markAllUnreadNotificationsAsRead(account.getId());
        return ResponseEntity.ok().build();
    }
}