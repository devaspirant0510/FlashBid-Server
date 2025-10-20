package seoil.capstone.flashbid.domain.auction.event;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import seoil.capstone.flashbid.domain.auction.dto.model.AuctionBidModel;
import seoil.capstone.flashbid.domain.auction.dto.model.AuctionChatModel;
import seoil.capstone.flashbid.domain.auction.entity.ConfirmedBidsEntity;
import seoil.capstone.flashbid.domain.auction.projection.UserMaxBidProjection;
import seoil.capstone.flashbid.domain.auction.service.AuctionService;
import seoil.capstone.flashbid.domain.payment.projection.UserPaymentProjection;
import seoil.capstone.flashbid.domain.payment.service.PaymentService;
import seoil.capstone.flashbid.global.common.enums.AuctionStatus;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuctionExpiredHandler {
    private final AuctionService auctionService;
    private final SimpMessagingTemplate messagingTemplate;
    private final PaymentService paymentService;


    @EventListener
    public void handleAuctionExpired(AuctionExpiredEvent event) {
        log.info("Auction expired event received:");
        log.info(event.toString());
        ConfirmedBidsEntity confirmedBidsEntity = auctionService.endAuction(event.auctionId());
        // TODO : 실시간으로 경매 완료 처리

        // 미 낙찰자 사용된 포인트 환불
        List<UserMaxBidProjection> topBiddersPaymentsByAuctionId = auctionService.getMaxBidPerUserByAuctionId(event.auctionId());

        int rank = 0;
        for (UserMaxBidProjection payment : topBiddersPaymentsByAuctionId) {
            if(rank==0){
                rank++;
                continue; // 낙찰자 제외
            }
            log.info("Processing refund for user: {}, paymentId: {}", payment.getBidderId(), payment.getMaxPrice());
            paymentService.refundPointsToUser(payment.getBidderId(), payment.getMaxPrice(), "경매 미 낙찰 포인트 환불");
        }


    }

    @EventListener
    public void handleAuctionStart(AuctionStartEvent event) {
        log.info("Auction start event received:");
        log.info(event.toString());
        // TODO : 해당 경매의 좋아요 한 목록을 불러와 시작 알림 전송
        //
        auctionService.startAuction(event.auctionId());

    }
}
