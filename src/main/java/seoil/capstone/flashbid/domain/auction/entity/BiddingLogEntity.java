package seoil.capstone.flashbid.domain.auction.entity;

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
@EntityListeners(AuditingEntityListener.class)
@Builder
@Entity(name = "bidding_log")
@ToString
public class BiddingLogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Account bidder;

    @ManyToOne
    private Auction auction;

    @CreatedDate
    private LocalDateTime createdAt;

    @Column
    private Long price;

    @Column
    private Long prevPrice;

}
