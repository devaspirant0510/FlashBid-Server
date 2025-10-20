package seoil.capstone.flashbid.domain.auction.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import seoil.capstone.flashbid.domain.category.entity.CategoryEntity;
import seoil.capstone.flashbid.domain.user.entity.Account;
import seoil.capstone.flashbid.global.common.enums.AuctionStatus;
import seoil.capstone.flashbid.global.common.enums.AuctionType;
import seoil.capstone.flashbid.global.core.BaseTimeEntity;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
public class Auction extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Account user;

    @ManyToOne
    private Goods goods;

    @Column
    private int viewCount;

    @Column
    private LocalDateTime startTime;

    @Column
    private LocalDateTime endTime;

    @Column
    private int startPrice;

    @Column
    private int bidUnit;

    @Column
    private int count;

    @Enumerated(EnumType.STRING)
    @Column(name = "auction_type", nullable = false, length = 20)

    private AuctionType auctionType;

    @Enumerated(EnumType.STRING)
    @Column(name = "auction_status", nullable = false, length = 20)

    private AuctionStatus auctionStatus;

    @ManyToOne
    private CategoryEntity category;

    @OneToOne
    private DeliveryInfoEntity deliveryInfo;

    @OneToOne
    private TradingAreaEntity tradingArea;


}
