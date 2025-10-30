package seoil.capstone.flashbid.domain.payment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import seoil.capstone.flashbid.domain.payment.dto.PaymentSuccessDto;
import seoil.capstone.flashbid.domain.payment.entity.PaymentEntity;
import seoil.capstone.flashbid.domain.payment.entity.PointHistoryEntity;
import seoil.capstone.flashbid.domain.payment.repository.PaymentRepository;
import seoil.capstone.flashbid.domain.payment.repository.PointHistoryRepository;
import seoil.capstone.flashbid.domain.user.entity.Account;


@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {
    private final PointHistoryRepository pointHistoryRepository;
    private final PaymentRepository paymentRepository;

    public Slice<PointHistoryEntity> getPointHistoryList(Account account, int page, int size) {
        return pointHistoryRepository.findAllByUserId_IdOrderByCreatedAtDesc(account.getId(), PageRequest.of(page, size));
    }


    @Transactional
    public PaymentEntity processPaymentSuccess(Account account, PaymentSuccessDto dto) {
        PointHistoryEntity savedPointHistory = PointHistoryEntity.builder()
                .userId(account)
                .chargeType(PointHistoryEntity.ChargeType.CHARGE)
                .contents("포인트 충전")
                .earnedPoint(dto.getPointAmount())
                .build();
        pointHistoryRepository.save(savedPointHistory);
        PaymentEntity savedPayment = PaymentEntity.builder()
                .pointHistory(savedPointHistory)
                .paymentKey(dto.getPaymentKey())
                .orderId(dto.getOrderId())
                .receiptId(dto.getReceiptId())
                .receiptUrl(dto.getReceiptUrl())
                .status(dto.getStatus())
                .userId(account)
                .baseAmount(dto.getPaymentAmount())
                .finalAmount(dto.getPaymentAmount())
                .chargedPoint(dto.getPointAmount())
                .method(dto.getMethod())
                .purchaseAt(dto.getPurchaseAt())
                .build();
        paymentRepository.save(savedPayment);
        account.setPoint(account.getPoint() + dto.getPointAmount());
        return savedPayment;
    }
}
