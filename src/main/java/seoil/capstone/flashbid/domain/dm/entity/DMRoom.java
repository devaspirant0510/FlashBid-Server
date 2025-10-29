package seoil.capstone.flashbid.domain.dm.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import seoil.capstone.flashbid.domain.auction.entity.Auction;
import seoil.capstone.flashbid.domain.user.entity.Account;
import seoil.capstone.flashbid.global.core.BaseTimeEntity;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "dm_room")
public class DMRoom extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 30)
    private String roomName;

    // 방의 종류: AUCTION (경매 관련) 또는 GENERAL (일반 채팅)
    @Enumerated(EnumType.STRING)
    @Column(name = "room_type", nullable = false, length = 20)
    @Builder.Default
    private RoomType roomType = RoomType.AFTER;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auction_id")
    private Auction auction;

    // 방장
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private Account owner;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<DMChat> chats;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<DMParticipate> participants;

    // 방의 종류
    public enum RoomType {
        DURING,   // 경매 낙찰 후 생성된 채팅방
        AFTER    // 일반 사용자 간 채팅방
    }
}