package seoil.capstone.flashbid.domain.notification.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import seoil.capstone.flashbid.domain.notification.entity.NotificationEntity;
import seoil.capstone.flashbid.domain.notification.repository.NotificationRepository;
import seoil.capstone.flashbid.infrastructure.firebasefcm.FcmService;

@RestController
@RequestMapping("api/v1/admin")
@RequiredArgsConstructor
public class AdminNotificationController {

    private final FcmService fcmService;
    private final NotificationRepository notificationRepository;

    /**
     * 전체 사용자에게 공지 발송 + DB 저장
     */
    @PostMapping("/notice")
    public String sendGlobalNotice(@RequestBody NoticeRequest request) {
        try {
            // 🔹 FCM 전체 발송 ("all" 토픽)
            fcmService.sendToTopic("all", request.getTitle(), request.getBody(), request.getLink()t);

            // 🔹 DB 저장 (user 없이)
            NotificationEntity notification = NotificationEntity.builder()
                    .title(request.getTitle())
                    .content(request.getBody())
                    .notificationType(NotificationEntity.NotificationType.ALL)
                    .account(null) // 전체 공지는 특정 유저 없음
                    .build();
            notificationRepository.save(notification);

            return "전체 공지가 발송 및 저장되었습니다.";
        } catch (Exception e) {
            e.printStackTrace();
            return "공지 발송 실패: " + e.getMessage();
        }
    }

    @lombok.Data
    public static class NoticeRequest {
        private String title;
        private String body;
        private String link;
    }
}
