package seoil.capstone.flashbid.domain.dm.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import seoil.capstone.flashbid.domain.user.entity.Account;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "dm_chat_read", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"dm_chat_id", "user_id"})
})
public class DMChatRead {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 읽은 메시지
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dm_chat_id")
    private DMChat dmChat;

    // 읽은 사용자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Account user;

    // 읽은 시간
    @CreatedDate
    private LocalDateTime readAt;
}