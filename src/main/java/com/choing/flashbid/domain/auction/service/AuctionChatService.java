package com.choing.flashbid.domain.auction.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.choing.flashbid.domain.auction.dto.model.AuctionChatModel;
import com.choing.flashbid.domain.auction.dto.response.AuctionChatDto;
import com.choing.flashbid.domain.auction.entity.Auction;
import com.choing.flashbid.domain.auction.entity.AuctionChatEntity;
import com.choing.flashbid.domain.auction.entity.BiddingLogEntity;
import com.choing.flashbid.domain.auction.projection.AuctionChatProjection;
import com.choing.flashbid.domain.auction.repository.jpa.AuctionBidLogRepository;
import com.choing.flashbid.domain.auction.repository.jpa.AuctionChatRepository;
import com.choing.flashbid.domain.auction.repository.jpa.AuctionRepository;
import com.choing.flashbid.domain.user.entity.Account;
import com.choing.flashbid.domain.user.repository.AccountRepository;
import com.choing.flashbid.global.common.enums.ChatType;
import com.choing.flashbid.global.common.error.ApiException;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuctionChatService {
    private final AuctionChatRepository auctionChatRepository;
    private final AuctionRepository auctionRepository;
    private final AccountRepository accountRepository;
    private final AuctionBidLogRepository auctionBidLogRepository;

    private final SimpMessagingTemplate messagingTemplate;

    public Slice<AuctionChatProjection> findAllAuctionChayByAuctionIdWithCursor(Long auctionId, Long cursorId, Integer size) {
        if (cursorId == null) {
            cursorId = auctionChatRepository.getAuctionChatLastId(auctionId) + 1;
        }
        log.info("cursorId ,{}",cursorId);

        return auctionChatRepository.findAllAuctionQueryWithCursor(
                auctionId,
                cursorId,
                PageRequest.of(0, size)
        );
    }

    public List<AuctionChatProjection> findAllAuctionChatByAuctionId(Long auctionId) {
        return auctionChatRepository.findAllAuctionQuery(auctionId);
    }

    @Transactional
    public AuctionChatDto saveAuctionChat(AuctionChatModel model, Long auctionId) {
        Auction auction = auctionRepository.findById(auctionId).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "", ""));
        Account account = accountRepository.findById(model.getUserId()).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "", ""));
        BiddingLogEntity biddingLogEntity = null;
        if (model.getBid() != null) {
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
                .chatType(model.getBid() == null ? ChatType.MESSAGE : ChatType.BID_LOG)
                .contents(model.getContents())
                .biddingLog(biddingLogEntity)
                .user(account)
                .build();
        AuctionChatEntity save = auctionChatRepository.save(chat);
        log.info("save chat {}", save);
        return AuctionChatDto.fromEntity(save);

    }
}
