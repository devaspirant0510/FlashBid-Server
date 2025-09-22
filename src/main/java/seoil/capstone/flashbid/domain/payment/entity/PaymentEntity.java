package seoil.capstone.flashbid.domain.payment.entity;

import jakarta.persistence.*;
import lombok.*;
import seoil.capstone.flashbid.domain.user.entity.Account;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "Payment")
@Table(name = "payment")
public class PaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 결제 ID

    @ManyToOne
    private Account userId; // 유저 ID

    @ManyToOne
    @JoinColumn(name = "point_history_id")
    private PointHistoryEntity pointHistory; // 포인트 결제 내역 참조

    @Column(name = "purchase_at", nullable = false)
    private LocalDateTime purchaseAt; // 결제 시간

    @Column(name = "base_amount", nullable = false)
    private Integer baseAmount; // 기본 결제 금액

    @Column(name = "final_amount", nullable = false)
    private Integer finalAmount; // 최종 결제 금액

    @Column
    private Integer chargedPoint; // 충전 포인트

    @Column(name = "method", nullable = false, length = 50)
    private String method; // 결제 수단

    @Column(name = "order_id", nullable = false, length = 255)
    private String orderId; // 주문 번호

    @Column(name = "payment_key", length = 255,nullable = true)
    private String paymentKey; // 결제 키

    @Column(name = "receipt_id", length = 255)
    private String receiptId; // 영수증 ID

    @Column(name = "receipt_url", length = 255)
    private String receiptUrl; // 영수증 URL

    @Column(name = "status", length = 20, nullable = false)
    private String status; // 결제 상태
}
