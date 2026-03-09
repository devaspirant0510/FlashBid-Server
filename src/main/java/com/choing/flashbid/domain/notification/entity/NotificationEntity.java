package com.choing.flashbid.domain.notification.entity;

import jakarta.persistence.*;
import lombok.*;
import com.choing.flashbid.domain.user.entity.Account;
import com.choing.flashbid.global.core.BaseTimeEntity;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "Notification")
@Table(name = "notification")
public class NotificationEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private String content;

    @Column
    @Enumerated()
    private NotificationType notificationType;

    @ManyToOne
    private Account account;

    @Column
    private String link;

    public enum NotificationType {
        AUCTION_ENDED, POINT, ALL
    }
}
