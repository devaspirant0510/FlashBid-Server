package seoil.capstone.flashbid.domain.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Builder
@Entity
@Table(name = "follow", uniqueConstraints = {
        @UniqueConstraint(
                name = "follow_uk", // 유니크 제약조건 이름
                columnNames = {"follower_id", "following_id"} // 두 컬럼의 조합을 유니크하게 설정
        )
})
public class FollowEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Account follower;

    @ManyToOne
    private Account following;

    @CreatedDate
    private LocalDateTime createdAt;
}
