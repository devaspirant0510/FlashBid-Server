package seoil.capstone.flashbid.domain.auction.dto.response;

import lombok.*;
import seoil.capstone.flashbid.domain.auction.projection.AuctionProjection;

import java.time.LocalDateTime;


@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuctionItemDto {
    private Long id;
    private String goodsTitle;
    private String goodsImageUrl;
    private String categoryName;
    private String bidderName;
    private Long currentPrice;
    private Long startPrice;
    private Integer participateCount;
    private Integer biddingCount;
    private Long viewCount;
    private Long likeCount;
    private Long chatMessagingCount;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Short status;

    public static AuctionItemDto from(AuctionProjection projection, Long viewCount){
        AuctionItemDto dto = new AuctionItemDto();
        dto.id = projection.getId();
        dto.goodsTitle = projection.getGoodsTitle();
        dto.goodsImageUrl = projection.getGoodsImageUrl();
        dto.categoryName = projection.getCategoryName();
        dto.bidderName = projection.getBidderName();
        dto.currentPrice = projection.getCurrentPrice();
        dto.startPrice = projection.getStartPrice();
        dto.participateCount = projection.getParticipateCount();
        dto.biddingCount = projection.getBiddingCount();
        dto.viewCount = viewCount; // Redis에서 가져온 값
        dto.likeCount = projection.getLikeCount();
        dto.chatMessagingCount = projection.getChatMessagingCount();
        dto.startTime = projection.getStartTime();
        dto.endTime = projection.getEndTime();
        dto.status = projection.getStatus();
        return dto;
    }
}
