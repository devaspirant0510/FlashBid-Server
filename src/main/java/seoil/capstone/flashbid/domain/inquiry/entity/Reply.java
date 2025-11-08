package seoil.capstone.flashbid.domain.inquiry.entity;

import jakarta.persistence.*;
import lombok.*;
import seoil.capstone.flashbid.domain.user.entity.Account;
import seoil.capstone.flashbid.global.core.BaseTimeEntity;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reply extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 답변 작성자 (관리자)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Account user;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;
}