package seoil.capstone.flashbid.domain.auction.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import seoil.capstone.flashbid.domain.user.entity.Account;
import seoil.capstone.flashbid.global.common.enums.ChatType;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "AuctionChat")
@Table(name = "auction_chat")
@EntityListeners(AuditingEntityListener.class)
public class AuctionChatEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Account user;

    @ManyToOne
    private Auction auction;

    @Column
    private String contents;

    @CreatedDate
    private LocalDateTime createdAt;

    @Enumerated()
    @Column(name = "chat_type", nullable = false, length = 20)
    private ChatType chatType;

    @ManyToOne
    private BiddingLogEntity biddingLog;
}
