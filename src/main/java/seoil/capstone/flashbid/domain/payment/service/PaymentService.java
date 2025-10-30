package seoil.capstone.flashbid.domain.payment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import seoil.capstone.flashbid.domain.auction.entity.BiddingLogEntity;
import seoil.capstone.flashbid.domain.auction.repository.AuctionBidLogRepository;
import seoil.capstone.flashbid.domain.payment.dto.PaymentSuccessDto;
import seoil.capstone.flashbid.domain.payment.entity.PaymentEntity;
import seoil.capstone.flashbid.domain.payment.entity.PointHistoryEntity;
import seoil.capstone.flashbid.domain.payment.projection.UserPaymentProjection;
import seoil.capstone.flashbid.domain.payment.repository.PaymentRepository;
import seoil.capstone.flashbid.domain.payment.repository.PointHistoryRepository;
import seoil.capstone.flashbid.domain.user.entity.Account;
import seoil.capstone.flashbid.domain.user.repository.AccountRepository;
import seoil.capstone.flashbid.global.common.error.ApiException;

import java.util.Collections;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {
    private final PointHistoryRepository pointHistoryRepository;
    private final PaymentRepository paymentRepository;
    private final AuctionBidLogRepository auctionBidLogRepository;
    private final AccountRepository accountRepository;

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


    public Long getLastBidPrice(Account account, Long auctionId) {
        // 거래 내역이 없는 경우
        if (!auctionBidLogRepository.existsByBidderIdAndAuctionId(account.getId(), auctionId)) {
            log.error("경매내역 조회 실패 해당 경매에 입찰한 기록이 없습니다");
            return 0L;
        }
        // 가장 최근 입찰가 조회
        return auctionBidLogRepository
                .findTop1ByBidderIdAndAuctionIdOrderByCreatedAtDesc(account.getId(), auctionId)
                .map(BiddingLogEntity::getPrice)
                .orElse(0L);
    }

    // 주어진 auctionId에 대해 최고 입찰자(들)를 찾고, 그들의 결제 충전 포인트 합계를 내림차순으로 반환
    public List<UserPaymentProjection> getTopBiddersPaymentsByAuctionId(Long auctionId) {
        List<Long> topBidders = auctionBidLogRepository.findTopBiddersByAuctionId(auctionId);
        if (topBidders == null || topBidders.isEmpty()) return Collections.emptyList();
        return paymentRepository.findTotalPointsByUserIdsOrderByTotalPointsDesc(topBidders);
    }

    @Transactional
    public void refundPointsToUser(Long bidderId, Long maxPrice, String contents) {
        Account bidder = accountRepository.findById(bidderId).orElseThrow(()->new ApiException(HttpStatus.NOT_FOUND,"사용자 찾을수 없음","사용자 정보를 찾을 수 없습니다"));
        pointHistoryRepository.save(
                PointHistoryEntity
                        .builder()
                        .userId(bidder)
                        .chargeType(PointHistoryEntity.ChargeType.REFUND)
                        .contents(contents)
                        .earnedPoint(maxPrice.intValue())
                        .build()
        );
        bidder.setPoint(bidder.getPoint() + maxPrice.intValue());
    }

    public boolean isPurchaseConfirmed(Long auctionId) {
        String searchContent = "경매_" + auctionId;

        return pointHistoryRepository.findAll().stream()
                .anyMatch(history ->
                        history.getChargeType() == PointHistoryEntity.ChargeType.PURCHASE &&
                                history.getContents() != null &&
                                history.getContents().equals(searchContent)
                );
    }
}
