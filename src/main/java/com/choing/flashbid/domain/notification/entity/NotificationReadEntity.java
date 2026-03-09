package com.choing.flashbid.domain.notification.entity;

import jakarta.persistence.*;
import lombok.*;
import com.choing.flashbid.domain.user.entity.Account;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "NotificationRead")
@Table(name = "notification_read")
public class NotificationReadEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Account account;

    @ManyToOne
    private NotificationEntity notification;

    @Column
    private LocalDateTime readAt;
}
