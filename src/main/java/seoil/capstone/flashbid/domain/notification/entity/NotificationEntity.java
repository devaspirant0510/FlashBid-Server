package seoil.capstone.flashbid.domain.notification.entity;

import jakarta.persistence.*;
import lombok.*;
import seoil.capstone.flashbid.domain.user.entity.Account;
import seoil.capstone.flashbid.global.core.BaseTimeEntity;

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


    public enum NotificationType {
        AUCTION_ENDED, POINT, ALL
    }
}
