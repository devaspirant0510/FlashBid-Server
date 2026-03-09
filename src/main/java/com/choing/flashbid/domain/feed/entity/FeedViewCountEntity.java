package com.choing.flashbid.domain.feed.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "FeedViewCount")
@Table(name = "feed_view_count")
public class FeedViewCountEntity {
    @Id
    @Column(name = "feed_id")
    private Long id;

    @Column
    private Long viewCount;

    @Column
    private LocalDateTime backupTime;

}
