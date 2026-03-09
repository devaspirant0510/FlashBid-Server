package com.choing.flashbid.domain.auction.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import com.choing.flashbid.domain.auction.dto.model.AuctionChatModel;
import com.choing.flashbid.domain.auction.dto.response.AuctionChatDto;
import com.choing.flashbid.domain.auction.entity.Auction;
import com.choing.flashbid.domain.auction.entity.BiddingLogEntity;
import com.choing.flashbid.domain.auction.entity.ConfirmedBidsEntity;
import com.choing.flashbid.domain.auction.repository.jpa.AuctionBidLogRepository;
import com.choing.flashbid.domain.auction.repository.jpa.AuctionRepository;
import com.choing.flashbid.domain.auction.service.AuctionChatService;
import com.choing.flashbid.global.common.error.ApiException;


@Controller
@RequiredArgsConstructor
@Slf4j
public class AuctionChatSocketController {
    private final AuctionChatService auctionChatService;
    private final AuctionRepository auctionRepository;
    private final AuctionBidLogRepository auctionBidLogRepository;

    @MessageMapping("/chat/send/{auctionId}")
    @SendTo("/topic/public/{auctionId}")
    public AuctionChatDto sendChat(@DestinationVariable Long auctionId, @Payload AuctionChatModel message) {
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
