package com.choing.flashbid.domain.auction.infra;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import com.choing.flashbid.domain.auction.entity.Auction;
import com.choing.flashbid.domain.auction.entity.AuctionWishListCountEntity;
import com.choing.flashbid.domain.auction.repository.jpa.AuctionRepository;
import com.choing.flashbid.domain.auction.repository.jpa.AuctionWishListCountRepository;
import com.choing.flashbid.global.base.BaseCounter;

@RequiredArgsConstructor
@Component
@Slf4j
public class SimpleCounter implements BaseCounter<Long> {
    private final AuctionWishListCountRepository auctionWishListCountRepository;
    private final AuctionRepository auctionRepository;

    @Override
    public long increase(Long auctionId) {
        AuctionWishListCountEntity countEntity = auctionWishListCountRepository.findById(auctionId).orElse(null);
        Auction auction = auctionRepository.findById(auctionId).orElseThrow();
        // 찜 카운트 업데이트
        if (countEntity != null) {
            countEntity.setCount(countEntity.getCount() + 1);
            return auctionWishListCountRepository.save(countEntity).getCount();

        } else {
            // 찜 카운트가 없으면 새로 생성
            AuctionWishListCountEntity newCountEntity = AuctionWishListCountEntity.builder()
                    .auction(auction)
                    .count(1L)
                    .build();
            return auctionWishListCountRepository.save(newCountEntity).getCount();
        }
    }

    @Override
    public long decrease(Long auctionId) {
        AuctionWishListCountEntity countEntity = auctionWishListCountRepository.findById(auctionId).orElseThrow();
        countEntity.setCount(countEntity.getCount() - 1);
        return auctionWishListCountRepository.save(countEntity).getCount();

    }

    @Override
    public long getCount(Long auctionId) {
        return auctionWishListCountRepository
                .countWishList(auctionId)
                .orElseGet(()->0L);
    }
}
