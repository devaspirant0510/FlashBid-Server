package seoil.capstone.flashbid.domain.feed.entity;

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
public class LikeEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @CreatedDate
    private LocalDateTime createdAt;

    @ManyToOne
    private Account account;

    @ManyToOne
    private FeedEntity feed;

}
