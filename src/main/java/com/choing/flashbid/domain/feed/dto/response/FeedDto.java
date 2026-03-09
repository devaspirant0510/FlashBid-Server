package com.choing.flashbid.domain.feed.dto.response;

import lombok.*;
import com.choing.flashbid.domain.feed.projection.FeedSummaryProjection;
import com.choing.flashbid.domain.file.projection.FileProjection;
import com.choing.flashbid.global.common.enums.AuctionStatus;
import com.choing.flashbid.global.common.enums.AuctionType;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FeedDto {
    private Long id;
    private String contents;
    private Long writerId;
    private String writerName;
    private String writerProfileImageUrl;
    private LocalDateTime createdAt;
    private Integer likeCount;
    private Integer commentCount;
    private Boolean liked;
    private List<FileProjection> images;
    private FeedAuctionDto feedAuction;
    private FeedConfirmBidDto feedConfirmBid;

    public static FeedDto from(FeedSummaryProjection summaryProjection, List<FileProjection> images) {
        FeedDto feedDto = new FeedDto();
        feedDto.feedAuction = FeedAuctionDto.from(summaryProjection);
        feedDto.feedConfirmBid = FeedConfirmBidDto.from(summaryProjection);
        feedDto.images = images;
        feedDto.setId(summaryProjection.getId());
        feedDto.setContents(summaryProjection.getContents());
        feedDto.setWriterId(summaryProjection.getWriterId());
        feedDto.setWriterName(summaryProjection.getWriterName());
        feedDto.setWriterProfileImageUrl(summaryProjection.getWriterProfileImageUrl());
        feedDto.setCreatedAt(summaryProjection.getCreatedAt());
        feedDto.setLikeCount(summaryProjection.getLikeCount());
        feedDto.setCommentCount(summaryProjection.getCommentCount());
        feedDto.setImages(images);
        feedDto.setLiked(summaryProjection.getLiked());
        return feedDto;
    }

}

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
class FeedAuctionDto {
    private Long auctionId;
    private String categoryName;
    private String title;
    private String description;
    private Integer likeCount;
    private Integer viewCount;
    private Integer startPrice;
    private Integer currentPrice;
    private AuctionStatus status;
    private AuctionType auctionType;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String imageUrl;

    public static FeedAuctionDto from(FeedSummaryProjection projection) {
        if (projection == null || projection.getAuctionId() == null) {
            return null;
        }

        FeedAuctionDto fa = new FeedAuctionDto();
        fa.setAuctionId(projection.getAuctionId());
        fa.setCategoryName(projection.getAuctionCategoryName());
        fa.setTitle(projection.getAuctionTitle());
        fa.setDescription(projection.getAuctionDescription());
        fa.setLikeCount(projection.getAuctionLikeCount());
        fa.setViewCount(projection.getAuctionViewCount());
        fa.setStartPrice(projection.getAuctionStartPrice());
        fa.setCurrentPrice(projection.getAuctionCurrentPrice());
        fa.setStatus(projection.getAuctionStatus());
        fa.setStartTime(projection.getAuctionStartTime());
        fa.setEndTime(projection.getAuctionEndTime());
        fa.setImageUrl(projection.getAuctionImageUrl());
        fa.setAuctionType(projection.getAuctionType());

        return fa;

    }
}

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
 class FeedConfirmBidDto {

    private Long confirmBidId;

    private Long auctionId;
    private AuctionType auctionType;
    private String categoryName;
    private String title;
    private String description;
    private Integer startPrice;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private Long confirmedBidPrice;
    private String bidderName;
    private String bidderProfileImage;

    private Integer biddingCount;
    private Integer chatCount;

    public static FeedConfirmBidDto from(FeedSummaryProjection projection) {
        if (projection == null || projection.getConfirmBidId() == null) {
            return null;
        }

        FeedConfirmBidDto dto = new FeedConfirmBidDto();
        dto.setConfirmBidId(projection.getConfirmBidId());

        dto.setAuctionId(projection.getConfirmBidAuctionId());
        dto.setAuctionType(projection.getConfirmBidAuctionType());
        dto.setCategoryName(projection.getConfirmBidAuctionCategoryName());
        dto.setTitle(projection.getConfirmBidAuctionTitle());
        dto.setDescription(projection.getConfirmBidAuctionDescription());
        dto.setStartPrice(projection.getConfirmBidAuctionStartPrice());
        dto.setStartTime(projection.getConfirmBidAuctionStartTime());
        dto.setEndTime(projection.getConfirmBidAuctionEndTime());

        dto.setConfirmedBidPrice(projection.getConfirmedBidPrice());
        dto.setBidderName(projection.getConfirmedBidBidderName());
        dto.setBidderProfileImage(projection.getConfirmedBidBidderProfileImage());

        dto.setBiddingCount(projection.getConfirmedBidBiddingCount());
        dto.setChatCount(projection.getConfirmedBidChatCount());

        return dto;
    }
}
