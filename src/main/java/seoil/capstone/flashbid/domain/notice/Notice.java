package seoil.capstone.flashbid.domain.notice;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notices")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 공지 유형 (예: "notice", "patch", "event" 등)
    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String title;

    @Lob
    @Column(nullable = false)
    private String content;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    // 조회수 필드 추가 (기본값 0)
    @Column(name = "view_count", nullable = false)
    private Integer viewCount = 0;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}