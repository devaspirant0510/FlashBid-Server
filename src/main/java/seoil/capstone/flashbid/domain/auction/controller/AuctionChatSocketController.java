package seoil.capstone.flashbid.domain.auction.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import seoil.capstone.flashbid.domain.auction.dto.model.AuctionChatModel;
import seoil.capstone.flashbid.domain.auction.entity.Auction;
import seoil.capstone.flashbid.domain.auction.entity.AuctionChatEntity;
import seoil.capstone.flashbid.domain.auction.entity.BiddingLogEntity;
import seoil.capstone.flashbid.domain.auction.entity.ConfirmedBidsEntity;
import seoil.capstone.flashbid.domain.auction.repository.AuctionBidLogRepository;
import seoil.capstone.flashbid.domain.auction.repository.AuctionRepository;
import seoil.capstone.flashbid.domain.auction.service.AuctionChatService;
import seoil.capstone.flashbid.global.common.error.ApiException;


@Controller
@RequiredArgsConstructor
@Slf4j
public class AuctionChatSocketController {
    private final AuctionChatService auctionChatService;
    private final AuctionRepository auctionRepository;
    private final AuctionBidLogRepository auctionBidLogRepository;

    @MessageMapping("/chat/send/{auctionId}")
    @SendTo("/topic/public/{auctionId}")
    public AuctionChatEntity sendChat(@DestinationVariable Long auctionId, @Payload AuctionChatModel message) {
        return auctionChatService.saveAuctionChat(message, auctionId);
    }

    @MessageMapping("/chat/confirm/{auctionId}")
    @SendTo("/topic/public/confirm/{auctionId}")
    public ConfirmedBidsEntity messageConfirmBid(@DestinationVariable Long auctionId, @Payload AuctionChatModel message) {
        log.info("confirm socket "+auctionId);
        return null;
    }

    @MessageMapping("/chat/price/{auctionId}")
    @SendTo("/topic/price/{auctionId}")
    public Long messageAuctionPrice(@DestinationVariable Long auctionId) {
        log.info("toic price" + auctionId);
        BiddingLogEntity auctionBid = auctionBidLogRepository.findTop1ByAuctionIdOrderByPriceDesc(auctionId);
        if (auctionBid == null) {
            Auction auction = auctionRepository.findById(auctionId).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "", ""));
            return (long) auction.getStartPrice();
        }
        log.error("socket ");
        log.info(auctionBid.getPrice().toString());
        return auctionBid.getPrice();
    }
}
