package seoil.capstone.flashbid.domain.payment.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import seoil.capstone.flashbid.domain.auction.entity.Auction;
import seoil.capstone.flashbid.domain.auction.entity.BiddingLogEntity;
import seoil.capstone.flashbid.domain.auction.entity.ConfirmedBidsEntity;
import seoil.capstone.flashbid.domain.auction.repository.AuctionRepository;
import seoil.capstone.flashbid.domain.auction.repository.BiddingLogRepository;
import seoil.capstone.flashbid.domain.auction.repository.ConfirmedBidsRepository;
import seoil.capstone.flashbid.domain.payment.dto.PurchaseConfirmRequest;
import seoil.capstone.flashbid.domain.payment.entity.PointHistoryEntity;
import seoil.capstone.flashbid.domain.payment.repository.PointHistoryRepository;
import seoil.capstone.flashbid.domain.user.entity.Account;
import seoil.capstone.flashbid.domain.user.repository.AccountRepository;
import seoil.capstone.flashbid.global.common.enums.AuctionStatus;

@Service
@RequiredArgsConstructor
@Transactional
public class AuctionPurchaseService {

    private final AuctionRepository auctionRepository;
    private final BiddingLogRepository biddingLogRepository;
    private final ConfirmedBidsRepository confirmedBidsRepository;
    private final AccountRepository accountRepository;
    private final PointHistoryRepository pointHistoryRepository;

    /**
     * 구매 확정 처리
     * 1. ConfirmedBidsEntity 생성
     * 2. 판매자 포인트 지급 (PointHistoryEntity 생성)
     * 3. 경매 상태 SOLD로 변경
     */
    @Transactional
    public void confirmPurchase(Account currentUser, PurchaseConfirmRequest request) {
        // 1. 경매 정보 조회
        Auction auction = auctionRepository.findById(request.getAuctionId())
                .orElseThrow(() -> new IllegalArgumentException("경매를 찾을 수 없습니다."));

        // 2. 판매자, 구매자 조회
        Account seller = accountRepository.findById(request.getSellerId())
                .orElseThrow(() -> new IllegalArgumentException("판매자를 찾을 수 없습니다."));

        Account bidder = accountRepository.findById(request.getBuyerId())
                .orElseThrow(() -> new IllegalArgumentException("구매자를 찾을 수 없습니다."));

        // 3. 권한 검증 - 현재 사용자가 구매자인지 확인
        if (!bidder.getId().equals(currentUser.getId())) {
            throw new IllegalArgumentException("구매자만 구매 확정을 할 수 있습니다.");
        }

        // 4. 경매 상태 검증 - ENDED 상태인지 확인
        if (auction.getAuctionStatus() != AuctionStatus.ENDED) {
            throw new IllegalArgumentException("경매가 종료되지 않았습니다.");
        }

//        // 6. 경매 종료 시간 검증
//        if (auction.getEndTime().isAfter(java.time.LocalDateTime.now())) {
//            throw new IllegalArgumentException("경매 종료 시간이 지나지 않았습니다.");
//        }

        // 7. 최신 입찰 로그 조회
        biddingLogRepository.findTopByAuctionOrderByIdDesc(auction)
                .orElseThrow(() -> new IllegalArgumentException("입찰 정보를 찾을 수 없습니다."));

        // 9. 판매자 포인트 지급
        grantSellerPoints(seller, request.getAmount(), auction.getId());
    }

    /**
     * 판매자에게 포인트 지급
     * PointHistoryEntity에 PURCHASE 타입으로 기록
     * contents에 경매 ID 포함하여 추후 조회 가능
     */
    private void grantSellerPoints(Account seller, Long amount, Long auctionId) {
        // 1. 판매자의 포인트 증가
        Integer currentPoint = seller.getPoint() != null ? seller.getPoint() : 0;
        seller.setPoint((int) (currentPoint + amount));
        accountRepository.save(seller);

        // 2. 포인트 이력 기록 - PURCHASE 타입으로 기록
        PointHistoryEntity pointHistory = PointHistoryEntity.builder()
                .userId(seller)
                .earnedPoint(Math.toIntExact(amount))
                .chargeType(PointHistoryEntity.ChargeType.PURCHASE)
                .contents("경매_" + auctionId)
                .build();
        pointHistoryRepository.save(pointHistory);
    }
}