package com.choing.flashbid.domain.auction.infra;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import com.choing.flashbid.domain.auction.repository.jpa.AuctionWishListCountRepository;
import com.choing.flashbid.global.base.BaseCounter;

@RequiredArgsConstructor
@Component
@Slf4j
public class UpsertCounter implements BaseCounter<Long> {
    private final AuctionWishListCountRepository  auctionWishListCountRepository;

    @Override
    public long increase(Long auctionId) {
        auctionWishListCountRepository.upsertWishListCount(auctionId);
        return getCount(auctionId);
    }

    @Override
    public long decrease(Long auctionId) {
        auctionWishListCountRepository.decrementCountByAuctionId(auctionId);
        return getCount(auctionId);
    }

    @Override
    public long getCount(Long auctionId) {
        return auctionWishListCountRepository
                .countWishList(auctionId)
                .orElseGet(()->0L);
    }
}
