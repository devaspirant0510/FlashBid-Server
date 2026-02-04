package seoil.capstone.flashbid.domain.auction.dto.response;

import lombok.*;
import seoil.capstone.flashbid.domain.auction.projection.AuctionDetailProjection;
import seoil.capstone.flashbid.global.common.enums.AuctionStatus;

import java.time.LocalDateTime;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AuctionDetailDto {
    private Long id;
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
    private Boolean isLiked;
    private Bidder bidder;

    @Getter
    @ToString
    public static class Bidder {
        private Long id;
        private String nickname;
        private Integer follower;
        private Integer following;
        private Integer confirmBidCount;
        private Integer saleCount;
    }

    public static AuctionDetailDto from(AuctionDetailProjection projection, Long viewCount) {
        AuctionDetailDto dto = new AuctionDetailDto();
        dto.id = projection.getId();
        dto.title = projection.getTitle();
        dto.description = projection.getDescription();
        dto.currentPrice = projection.getCurrentPrice();
        dto.startPrice = projection.getStartPrice();
        dto.participateCount = projection.getParticipateCount();
        dto.biddingCount = projection.getBiddingCount();
        dto.viewCount = viewCount;
        dto.likeCount = projection.getLikeCount();
        dto.chatMessagingCount = projection.getChatMessagingCount();
        dto.startTime = projection.getStartTime();
        dto.endTime = projection.getEndTime();
        dto.status = projection.getStatus();
        dto.categoryName = projection.getCategoryName();
        dto.isLiked = projection.getIsLiked();
        dto.bidder = new Bidder();
        dto.bidder.id = projection.getBidderId();
        dto.bidder.nickname = projection.getBidderNickname();
        dto.bidder.confirmBidCount = projection.getBidderConfirmBidCount();
        dto.bidder.saleCount = projection.getBidderSaleCount();
        dto.bidder.follower = projection.getBidderFollower();
        dto.bidder.following = projection.getBidderFollowing();
        return dto;
    }
}
