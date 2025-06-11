package seoil.capstone.flashbid.domain.auction.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import seoil.capstone.flashbid.domain.user.entity.Account;
import seoil.capstone.flashbid.global.core.BaseTimeEntity;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@Entity(name = "auction_participate")
public class AuctionParticipateEntity  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Account participant;

    @ManyToOne
    private Auction auction;

    @CreatedDate
    private LocalDateTime joinTime;
}
