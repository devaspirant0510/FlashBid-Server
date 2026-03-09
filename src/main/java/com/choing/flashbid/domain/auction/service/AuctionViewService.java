package com.choing.flashbid.domain.auction.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.choing.flashbid.domain.auction.dto.response.ViewCountIncreasedDto;
import com.choing.flashbid.domain.auction.dto.response.ViewCountResultDto;
import com.choing.flashbid.domain.auction.entity.AuctionViewCountEntity;
import com.choing.flashbid.domain.auction.repository.jpa.AuctionRepository;
import com.choing.flashbid.domain.auction.repository.jpa.BackUpAuctionViewCountRepository;
import com.choing.flashbid.domain.auction.repository.redis.AuctionViewCountRepository;
import com.choing.flashbid.domain.auction.repository.redis.ViewCountVerificationRepository;
import com.choing.flashbid.domain.user.entity.Account;
import com.choing.flashbid.global.common.error.NotFoundAuctionException;
import com.choing.flashbid.global.core.provider.ClientIdentifierProvider;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuctionViewService {
    private final AuctionViewCountRepository auctionViewCountRepository;
    private final ViewCountVerificationRepository viewCountVerificationRepository;
    private final BackUpAuctionViewCountRepository backUpAuctionViewCountRepository;
    private final ClientIdentifierProvider clientIdentifierProvider;
    private final AuctionRepository auctionRepository;

    public ViewCountIncreasedDto increaseView(Long auctionId, Account account, HttpServletRequest request) {
        if (!auctionRepository.existsById(auctionId)) {
            throw new NotFoundAuctionException();
        }
        if (account == null) {
            return increaseAuctionView(auctionId, clientIdentifierProvider.extractClientIp(request), clientIdentifierProvider.extractUserAgent(request));
        }
        return increaseAuctionView(auctionId, account.getId());
    }

    private ViewCountIncreasedDto increaseAuctionView(Long auctionId, Long userId) {
        // 처음 집계되는 조회수일경우 조회수 증가
        if (viewCountVerificationRepository.isFirstView(auctionId, userId)) {
            auctionViewCountRepository.increase(auctionId);
            return ViewCountIncreasedDto.create(true);
        }
        return ViewCountIncreasedDto.create(false);
    }

    private ViewCountIncreasedDto increaseAuctionView(Long auctionId, String ip, String agent) {
        // 처음 집계되는 조회수일경우 조회수 증가
        if (viewCountVerificationRepository.isFirstView(auctionId, ip, agent)) {
            auctionViewCountRepository.increase(auctionId);
            return ViewCountIncreasedDto.create(true);
        }
        return ViewCountIncreasedDto.create(false);
    }

    public ViewCountResultDto getAuctionViewCount(Long auctionId) {
        Long viewCountWithMemory = auctionViewCountRepository.getViewCount(auctionId);
        // redis 에 조회수가 있을경우 레디스 데이터 리턴
        if (viewCountWithMemory != null) {
            return ViewCountResultDto.create(viewCountWithMemory);
        }
        // redis 데이터 존재하지 않을시 백업디비에서 리턴, 백업디비에도 존재하지 않을경우 0 리턴
        AuctionViewCountEntity viewCountWithDB = backUpAuctionViewCountRepository
                .findById(auctionId)
                .orElseGet(() -> {
                    AuctionViewCountEntity empty = AuctionViewCountEntity.empty();
                    backUpAuctionViewCountRepository.save(empty);
                    return empty;
                });

        return ViewCountResultDto.create(
                viewCountWithDB.getViewCount()
        );
    }


}
