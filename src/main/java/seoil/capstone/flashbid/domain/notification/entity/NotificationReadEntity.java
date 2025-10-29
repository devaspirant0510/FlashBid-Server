package seoil.capstone.flashbid.domain.notification.entity;

import jakarta.persistence.*;
import lombok.*;
import seoil.capstone.flashbid.domain.user.entity.Account;

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

    @Column
    private LocalDateTime readAt;
}
