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
public class Inquiry extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 문의 작성자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Account user;

    // 답변 (1:1 관계)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reply_id")
    private Reply reply;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InquiryType inquiryType;

    @Column(nullable = false)
    @Builder.Default
    private boolean replyStatus = false;

    public void linkReply(Reply reply) {
        this.reply = reply;
        this.replyStatus = true;
    }

    public void unlinkReply() {
        this.reply = null;
        this.replyStatus = false;
    }

    public boolean isOwnedBy(Long userId) {
        return this.user != null && this.user.getId().equals(userId);
    }
}