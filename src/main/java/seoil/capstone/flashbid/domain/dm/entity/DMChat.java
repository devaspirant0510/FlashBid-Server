package seoil.capstone.flashbid.domain.dm.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import seoil.capstone.flashbid.domain.user.entity.Account;
import seoil.capstone.flashbid.global.core.BaseTimeEntity;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "dm_chat")
public class DMChat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 송신자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private Account sender;

    // 수신자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private Account receiver;

    // 채팅방
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private DMRoom room;

    @Column(columnDefinition = "TEXT")
    private String contents;

    @Enumerated(EnumType.STRING)
    @Column(name = "dm_type", nullable = false, length = 20)

    private DMType dmType;  // MESSAGE, FILE 등

    @CreatedDate
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "dmChat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DMImage> images;

    @OneToMany(mappedBy = "dmChat", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<DMChatRead> reads;

    public enum DMType {
        MESSAGE,
        FILE
    }
}

