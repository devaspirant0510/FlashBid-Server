package seoil.capstone.flashbid.domain.payment.entity;

import jakarta.persistence.*;
import lombok.*;
import seoil.capstone.flashbid.domain.user.entity.Account;
import seoil.capstone.flashbid.global.core.BaseTimeOnlyCreated;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "PointHistory")
@Table(name = "point_history")
public class PointHistoryEntity extends BaseTimeOnlyCreated {
    public enum ChargeType {
        CHARGE,
        GIFT,
        PURCHASE
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 포인트 결제 내역 ID


    @Column(name = "earned_point", nullable = false)
    private Integer earnedPoint; // 충전 포인트

    @Enumerated(EnumType.STRING)
    @Column(name = "charge_type", nullable = false, length = 20)
    private ChargeType chargeType; // 충전 유형 (CHARGE, GIFT, PURCHASE)

    @Column(name = "contents", length = 1024)
    private String contents; // 충전 내용

    @ManyToOne
    private Account userId; // 충전한 유저 ID
}
