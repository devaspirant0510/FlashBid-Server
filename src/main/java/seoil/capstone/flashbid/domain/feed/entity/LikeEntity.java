package seoil.capstone.flashbid.domain.feed.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import seoil.capstone.flashbid.domain.user.entity.Account;
import seoil.capstone.flashbid.global.core.BaseTimeEntity;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@Entity(name = "likes")
@Table(
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"account_id", "feed_id"})
        }
)
@Schema(description = "좋아요 엔티티")
public class LikeEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Schema(description = "좋아요 ID", example = "1")
    private Long id;

    @CreatedDate
    @Schema(description = "좋아요 누른 시각", example = "2025-06-06T15:23:45")
    private LocalDateTime createdAt;

    @ManyToOne
    @Schema(description = "좋아요 누른 계정")
    private Account account;

    @ManyToOne
    @Schema(description = "좋아요가 눌린 피드")
    private FeedEntity feed;
}
