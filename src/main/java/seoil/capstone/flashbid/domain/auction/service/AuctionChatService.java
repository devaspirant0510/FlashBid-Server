package seoil.capstone.flashbid.domain.auction.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import seoil.capstone.flashbid.domain.auction.dto.model.AuctionChatModel;
import seoil.capstone.flashbid.domain.auction.entity.Auction;
import seoil.capstone.flashbid.domain.auction.entity.AuctionChatEntity;
import seoil.capstone.flashbid.domain.auction.entity.BiddingLogEntity;
import seoil.capstone.flashbid.domain.auction.repository.AuctionBidLogRepository;
import seoil.capstone.flashbid.domain.auction.repository.AuctionChatRepository;
import seoil.capstone.flashbid.domain.auction.repository.AuctionRepository;
import seoil.capstone.flashbid.domain.user.entity.Account;
import seoil.capstone.flashbid.domain.user.repository.AccountRepository;
import seoil.capstone.flashbid.global.common.enums.ChatType;
import seoil.capstone.flashbid.global.common.error.ApiException;

import java.util.HashMap;
import java.util.Map;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuctionChatService {
    private final AuctionChatRepository auctionChatRepository;
    private final AuctionRepository auctionRepository;
    private final AccountRepository accountRepository;
    private final AuctionBidLogRepository auctionBidLogRepository;

    private final SimpMessagingTemplate messagingTemplate;

    @Transactional
    public AuctionChatEntity saveAuctionChat(AuctionChatModel model,Long auctionId){
        Auction auction = auctionRepository.findById(auctionId).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "", ""));
        Account account = accountRepository.findById(model.getUserId()).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "", ""));
        BiddingLogEntity biddingLogEntity = null;
        if (model.getBid()!=null){
            BiddingLogEntity bidlog = BiddingLogEntity.builder()
                    .auction(auction)
                    .bidder(account)
                    .prevPrice(model.getBid().getPrevPrice())
                    .price(model.getBid().getPrice())
                    .build();
            biddingLogEntity = auctionBidLogRepository.save(bidlog);
        }


        AuctionChatEntity chat = AuctionChatEntity.builder()
                .auction(auction)
                .chatType(model.getBid()==null?ChatType.MESSAGE:ChatType.BID_LOG)
                .contents(model.getContents())
                .biddingLog(biddingLogEntity)
                .user(account)
                .build();
        Map<String, Object> priceData = new HashMap<>();
        Message<String> message = new Message<String>() {
            @Override
            public String getPayload() {
                return "";
            }

            @Override
            public MessageHeaders getHeaders() {
                return null;
            }
        };
        AuctionChatEntity save = auctionChatRepository.save(chat);
        return save;

    }
}
