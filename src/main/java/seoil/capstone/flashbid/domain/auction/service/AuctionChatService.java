package seoil.capstone.flashbid.domain.auction.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import seoil.capstone.flashbid.domain.auction.dto.model.AuctionChatModel;
import seoil.capstone.flashbid.domain.auction.entity.Auction;
import seoil.capstone.flashbid.domain.auction.entity.AuctionChatEntity;
import seoil.capstone.flashbid.domain.auction.repository.AuctionChatRepository;
import seoil.capstone.flashbid.domain.auction.repository.AuctionRepository;
import seoil.capstone.flashbid.domain.user.entity.Account;
import seoil.capstone.flashbid.domain.user.repository.AccountRepository;
import seoil.capstone.flashbid.global.common.enums.ChatType;
import seoil.capstone.flashbid.global.common.error.ApiException;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuctionChatService {
    private final AuctionChatRepository auctionChatRepository;
    private final AuctionRepository auctionRepository;
    private final AccountRepository accountRepository;

    @Transactional
    public AuctionChatEntity saveAuctionChat(AuctionChatModel model,Long auctionId){
        Auction auction = auctionRepository.findById(auctionId).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "", ""));
        Account account = accountRepository.findById(model.getUserId()).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "", ""));


        AuctionChatEntity chat = AuctionChatEntity.builder()
                .auction(auction)
                .chatType(ChatType.MESSAGE)
                .contents(model.getContents())
                .user(account)
                .build();
        return auctionChatRepository.save(chat);

    }
}
