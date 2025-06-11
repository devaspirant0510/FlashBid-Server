package seoil.capstone.flashbid.domain.auction.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import seoil.capstone.flashbid.domain.auction.dto.model.AuctionChatModel;
import seoil.capstone.flashbid.domain.auction.entity.AuctionChatEntity;
import seoil.capstone.flashbid.domain.auction.service.AuctionChatService;


@Controller
@RequiredArgsConstructor
@Slf4j
public class AuctionChatSocketController {
    private final AuctionChatService auctionChatService;

    @MessageMapping("/chat/send/{auctionId}")
    @SendTo("/topic/public/{auctionId}")
    public AuctionChatEntity sendChat(@DestinationVariable Long auctionId, @Payload AuctionChatModel message) {
       return  auctionChatService.saveAuctionChat(message,auctionId);
    }
}
