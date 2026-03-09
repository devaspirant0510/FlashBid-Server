package com.choing.flashbid.domain.auction.entity;


import jakarta.persistence.*;
import lombok.*;
import com.choing.flashbid.domain.category.entity.CategoryEntity;
import com.choing.flashbid.domain.user.entity.Account;
import com.choing.flashbid.global.common.enums.AuctionStatus;
import com.choing.flashbid.global.common.enums.AuctionType;
import com.choing.flashbid.global.core.BaseTimeEntity;

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
    private Long id;

    @ManyToOne
    private Account user;

    @ManyToOne
    private Goods goods;

    @Column
    private LocalDateTime startTime;

    @Column
    private LocalDateTime endTime;

    @Column
    private int startPrice;

    @Column
    private int bidUnit;

    @Column
    private int count; // 참여자수

    @Enumerated
    @Column(name = "auction_type", nullable = false, length = 20)
    private AuctionType auctionType;

    @Enumerated
    @Column(name = "auction_status", nullable = false, length = 20)

    private AuctionStatus auctionStatus;

    @ManyToOne
    private CategoryEntity category;

    @OneToOne
    private DeliveryInfoEntity deliveryInfo;

    @OneToOne
    private TradingAreaEntity tradingArea;


}
