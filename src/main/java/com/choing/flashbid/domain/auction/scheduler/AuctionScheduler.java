//package com.choing.flashbid.domain.auction.scheduler;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//import com.choing.flashbid.domain.auction.entity.Auction;
//import com.choing.flashbid.domain.auction.entity.BiddingLogEntity;
//import com.choing.flashbid.domain.auction.repository.jpa.AuctionRepository;
//import com.choing.flashbid.domain.auction.repository.jpa.AuctionBidLogRepository;
//import com.choing.flashbid.domain.auction.service.AuctionService;
//import com.choing.flashbid.domain.user.entity.Account;
//import com.choing.flashbid.global.common.error.ApiException;
//import org.springframework.http.HttpStatus;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class AuctionScheduler {
//
//    private final AuctionRepository auctionRepository;
//    private final AuctionBidLogRepository auctionBidLogRepository;
//    private final AuctionService auctionService;
//
//    /**
//     * 5분마다 종료된 경매 자동 처리
//     */
//    @Scheduled(fixedRate = 300000) // 5분마다 실행 (1000 * 60 * 5)
//    @Transactional
//    public void processEndedAuctions() {
//        LocalDateTime now = LocalDateTime.now();
//
//        // 1️⃣ 아직 낙찰 확정 안 된 종료된 경매 조회
//        List<Auction> endedAuctions = auctionRepository.findAllByEndTimeBeforeAndIdNotInConfirmedBids(now);
//
//        for (Auction auction : endedAuctions) {
//            try {
//                // 2️⃣ 최고가 입찰자 찾기
//                BiddingLogEntity topBid = auctionBidLogRepository
//                        .findTop1ByAuctionIdOrderByPriceDesc(auction.getId());
//
//                if (topBid == null) {
//                    log.info("❌ 경매 {}: 입찰자가 없어 자동 확정 생략", auction.getId());
//                    continue;
//                }
//
//                Account bidder = topBid.getUser();
//                log.info("✅ 경매 {} 자동 낙찰 확정: {}", auction.getId(), bidder.getNickname());
//
//                // 3️⃣ 자동 낙찰 확정 + DM 생성
//                auctionService.confirmedBidsEntity(
//                        bidder,
//                        auction.getId(),
//                        topBid.getId()
//                );
//            } catch (Exception e) {
//                log.error("⚠️ 경매 {} 자동 처리 실패: {}", auction.getId(), e.getMessage());
//            }
//        }
//    }
//}
