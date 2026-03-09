package com.choing.flashbid.domain.feed.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import jakarta.persistence.Entity;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "FeedLikeCount")
@Table(name = "feed_like_count")
public class FeedLikeCountEntity {
    @Id
    @Column(name = "feed_id")
    private Long id;

    @Column
    private Integer likeCount;
}
