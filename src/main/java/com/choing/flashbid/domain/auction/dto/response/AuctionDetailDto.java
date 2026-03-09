package com.choing.flashbid.domain.auction.dto.response;

import lombok.*;
import com.choing.flashbid.domain.auction.projection.AuctionDetailProjection;
import com.choing.flashbid.global.common.enums.AuctionStatus;
import com.choing.flashbid.global.common.enums.AuctionType;
import com.choing.flashbid.global.common.enums.EnumConvertor;

import java.time.LocalDateTime;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AuctionDetailDto {
    private String id;
    private String title;
    private String categoryName;
    private String description;
    private Long currentPrice;
    private Long startPrice;
    private Integer participateCount;
    private Integer biddingCount;
    private Long viewCount;
    private Long likeCount;
    private Long chatMessagingCount;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private AuctionStatus status;
    private AuctionType auctionType;
    private Boolean isLiked;
    private Bidder bidder;

    @Getter
    @ToString
    public static class Bidder {
        private String id;
        private String nickname;
        private Integer follower;
        private Integer following;
        private Integer confirmBidCount;
        private Integer saleCount;
    }

    public static AuctionDetailDto from(AuctionDetailProjection projection, Long viewCount) {
        AuctionDetailDto dto = new AuctionDetailDto();

        dto.status = EnumConvertor.fromCode(AuctionStatus.class,projection.getStatus());
        dto.auctionType = EnumConvertor.fromCode(AuctionType.class,projection.getAuctionType());
        dto.id = projection.getId().toString();
        dto.title = projection.getTitle();
        dto.description = projection.getDescription();
        dto.currentPrice = dto.auctionType==AuctionType.LIVE?projection.getCurrentPrice():-1;
        dto.startPrice = projection.getStartPrice();
        dto.participateCount = projection.getParticipateCount();
        dto.biddingCount = projection.getBiddingCount();
        dto.viewCount = viewCount;
        dto.likeCount = projection.getLikeCount();
        dto.chatMessagingCount = projection.getChatMessagingCount();
        dto.startTime = projection.getStartTime();
        dto.endTime = projection.getEndTime();
        dto.categoryName = projection.getCategoryName();
        dto.isLiked = projection.getIsLiked();
        dto.bidder = new Bidder();
        dto.bidder.id = projection.getBidderId().toString();
        dto.bidder.nickname = projection.getBidderNickname();
        dto.bidder.confirmBidCount = projection.getBidderConfirmBidCount();
        dto.bidder.saleCount = projection.getBidderSaleCount();
        dto.bidder.follower = projection.getBidderFollower();
        dto.bidder.following = projection.getBidderFollowing();
        return dto;
    }
}
