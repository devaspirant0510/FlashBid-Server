package seoil.capstone.flashbid.domain.feed.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import seoil.capstone.flashbid.domain.feed.entity.FeedEntity;
import seoil.capstone.flashbid.domain.feed.projection.FeedProjection;
import seoil.capstone.flashbid.domain.file.projection.FileProjection;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FeedListResponse {
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

    public static FeedListResponse from(FeedProjection projection, List<FileProjection> images) {
        FeedListResponse response = new FeedListResponse();
        response.setId(projection.getId());
        response.setContents(projection.getContents());
        response.setWriterId(projection.getWriterId());
        response.setWriterName(projection.getWriterName());
        response.setWriterProfileImageUrl(projection.getWriterProfileImageUrl());
        response.setCreatedAt(projection.getCreatedAt());
        response.setLikeCount(projection.getLikeCount());
        response.setCommentCount(projection.getCommentCount());
        response.setImages(images);
        response.setLiked(projection.getLiked());
        return response;
    }

    public static FeedListResponse from(FeedEntity feed,List<FileProjection> images) {
        FeedListResponse response = new FeedListResponse();
        response.setId(feed.getId());
        response.setContents(feed.getContents());
        response.setWriterId(feed.getUser().getId());
        response.setWriterName(feed.getUser().getNickname());
        response.setWriterProfileImageUrl(feed.getUser().getProfileUrl());
        response.setCreatedAt(feed.getCreatedAt());
        response.setLikeCount(0);
        response.setCommentCount(0);
        response.setLiked(false);
        response.setImages(images);
        return response;
    }

}
