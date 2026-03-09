package com.choing.flashbid.domain.auction.infra;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.choing.flashbid.domain.auction.entity.AuctionWishListCountEntity;
import com.choing.flashbid.domain.auction.repository.jpa.AuctionRepository;
import com.choing.flashbid.domain.auction.repository.jpa.AuctionWishListCountRepository;
import com.choing.flashbid.global.base.BaseCounter;
import com.choing.flashbid.global.common.error.ApiException;
import com.choing.flashbid.global.common.error.NotFoundAuctionException;

@Component
@Primary
@RequiredArgsConstructor
@Slf4j
public class WishListPessimisticLockCounter implements BaseCounter<Long> {
    private final AuctionWishListCountRepository auctionWishListCountRepository;
    private final AuctionRepository auctionRepository;


    @Retryable(
            retryFor = {DataIntegrityViolationException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 100)
    )
    @Override
    @Transactional
    public long increase(Long auctionId) {
        AuctionWishListCountEntity wishListCount = auctionWishListCountRepository
                .findByAuctionIdWithPessimisticLock(auctionId)
                .orElseGet(() -> {
                    return auctionWishListCountRepository.save(AuctionWishListCountEntity.init(auctionRepository.findById(auctionId).orElseThrow()));
                });
        wishListCount.setCount(wishListCount.getCount() + 1);
        auctionWishListCountRepository.save(wishListCount);
        return getCount(auctionId);
    }

    @Override
    @Transactional
    public long decrease(Long auctionId) {
        auctionRepository.findById(auctionId).orElseThrow(NotFoundAuctionException::new);
        AuctionWishListCountEntity wishListCount = auctionWishListCountRepository
                .findByAuctionIdWithPessimisticLock(auctionId)
                .orElseThrow(()->new ApiException(400,"잘못된 요청입니다.","잘못된 요청입니다."));
        wishListCount.setCount(wishListCount.getCount() - 1);
        auctionWishListCountRepository.save(wishListCount);
        return wishListCount.getCount();
    }

    @Override
    public long getCount(Long auctionId) {
        return auctionWishListCountRepository
                .countWishList(auctionId)
                .orElseGet(() -> 0L);
    }
}
