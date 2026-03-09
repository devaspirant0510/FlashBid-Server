package com.choing.flashbid.domain.admin.dto.response;

import lombok.*;
import com.choing.flashbid.domain.admin.projection.AuctionAdminProjection;
import com.choing.flashbid.global.common.enums.AuctionStatus;
import com.choing.flashbid.global.common.enums.AuctionType;
import com.choing.flashbid.global.common.enums.EnumConvertor;

import java.time.LocalDateTime;


@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminAuctionDto {
    private Long auctionId;
    private String goodsTitle;
    private String sellerNickname;
    private String categoryName;

    private Integer startPrice;
    private Long lastBidAmount;

    private Long biddingCount;
    private Long participantsCount;
    private Long chatCount;

    private Integer viewCount;

    private AuctionType auctionType;
    private AuctionStatus auctionStatus;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime createdAt;

    public static AdminAuctionDto from(AuctionAdminProjection p) {
        return AdminAuctionDto.builder()
                .auctionId(p.getAuctionId())
                .goodsTitle(p.getGoodsTitle())
                .sellerNickname(p.getSellerNickname())
                .categoryName(p.getCategoryName())

                .startPrice(p.getStartPrice())
                .lastBidAmount(p.getLastBidAmount())

                .biddingCount(p.getBiddingCount())
                .participantsCount(p.getParticipantsCount())
                .chatCount(p.getChatCount())

                .viewCount(p.getViewCount())

                .auctionType(EnumConvertor.fromCode(AuctionType.class,p.getAuctionType()))
                .auctionStatus(EnumConvertor.fromCode(AuctionStatus.class,p.getAuctionStatus()))

                .startTime(p.getStartTime())
                .endTime(p.getEndTime())
                .createdAt(p.getCreatedAt())
                .build();
    }
}
