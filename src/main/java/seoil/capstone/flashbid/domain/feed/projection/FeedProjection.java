package seoil.capstone.flashbid.domain.feed.projection;

import java.time.LocalDateTime;

public interface FeedProjection {
    Long getId();
    String getContents();
    Long getWriterId();
    String getWriterName();
    String getWriterProfileImageUrl();
    LocalDateTime getCreatedAt();
    Integer getLikeCount();
    Integer getCommentCount();
    Boolean getLiked();
}
