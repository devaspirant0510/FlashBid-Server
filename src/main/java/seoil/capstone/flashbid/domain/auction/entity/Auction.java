package seoil.capstone.flashbid.domain.auction.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import seoil.capstone.flashbid.domain.user.entity.Account;
import seoil.capstone.flashbid.global.core.BaseTimeEntity;

import java.time.LocalDateTime;

@Entity
public class Auction extends BaseTimeEntity {
    @Id
    private Long id;

    @ManyToOne
    private Account user;

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



}
