package seoil.capstone.flashbid.domain.feed.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import seoil.capstone.flashbid.domain.user.entity.Account;
import seoil.capstone.flashbid.global.core.BaseTimeEntity;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "comments")
@Entity
@Schema(description = "댓글 엔티티")
public class CommentEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "댓글 ID", example = "1")
    private Long id;

    @ManyToOne
    @Schema(description = "댓글이 달린 피드")
    private FeedEntity feed;

    @ManyToOne
    @Schema(description = "작성자 계정")
    private Account user;

    @ManyToOne
    @Schema(description = "대댓글의 부모 댓글 (null이면 일반 댓글)")
    private CommentEntity reply;

    @Column
    @Schema(description = "댓글 내용", example = "이 경매 너무 재밌어요!")
    private String contents;
}
