package com.choing.flashbid.domain.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.choing.flashbid.domain.notification.entity.NotificationEntity;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationResponseDto {
    private Long id;
    private String title;
    private String content;
    private String notificationType;
    private String link;
    private LocalDateTime createdAt;
    private Boolean isRead;

    public static NotificationResponseDto from(NotificationEntity entity, boolean isRead) {
        return NotificationResponseDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .content(entity.getContent())
                .notificationType(entity.getNotificationType().name())
                .link(entity.getLink())
                .createdAt(entity.getCreatedAt())
                .isRead(isRead)
                .build();
    }
}