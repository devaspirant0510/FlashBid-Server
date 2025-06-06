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
@Entity(name = "feed")
@Schema(description = "피드 엔티티", title = "Feed")
public class FeedEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "피드 ID", example = "1")
    private Long id;

    @ManyToOne
    @Schema(description = "작성자 계정")
    private Account user;

    @Column
    @Schema(description = "피드 내용", example = "오늘은 낙찰 성공했어요!")
    private String contents;

    @Column
    @Schema(description = "조회수", example = "42")
    private int viewCount;
}
