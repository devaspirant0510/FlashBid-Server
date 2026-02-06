package seoil.capstone.flashbid.domain.auction.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import seoil.capstone.flashbid.domain.auction.entity.Auction;
import seoil.capstone.flashbid.domain.auction.entity.AuctionWishListEntity;
import seoil.capstone.flashbid.domain.auction.repository.jpa.AuctionRepository;
import seoil.capstone.flashbid.domain.auction.repository.jpa.AuctionWishListCountRepository;
import seoil.capstone.flashbid.domain.auction.repository.jpa.AuctionWishListRepository;
import seoil.capstone.flashbid.domain.user.entity.Account;
import seoil.capstone.flashbid.global.base.BaseCounter;
import seoil.capstone.flashbid.global.common.error.ApiException;
import seoil.capstone.flashbid.global.common.error.NotFoundAuctionException;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuctionWishListService {
    private final BaseCounter<Long> counter;
    private final AuctionWishListRepository auctionWishListRepository;
    private final AuctionRepository auctionRepository;
    private final AuctionWishListCountRepository auctionWishListCountRepository;

    @Transactional
    public long increase(Account account, Long auctionId, BaseCounter<Long> counter) {
        saveWishedAuction(account, auctionId);
        return counter.increase(auctionId);
    }

    @Transactional
    public long decrease(Account account, Long auctionId, BaseCounter<Long> counter) {
        saveWishedAuction(account, auctionId);
        return counter.decrease(auctionId);
    }

    @Transactional
    public long increase(Account account, Long auctionId) {
        saveWishedAuction(account, auctionId);
        return counter.increase(auctionId);
    }

    @Transactional
    public long decrease(Account account, Long auctionId) {
        deletedWishedAuction(account, auctionId);
        return counter.decrease(auctionId);
    }

    @Transactional
    protected void saveWishedAuction(Account account, Long auctionId) {
        Auction wishedAuction = auctionRepository.findById(auctionId).orElseThrow(NotFoundAuctionException::new);
        if (auctionWishListRepository.existsByUserIdAndAuctionId(account.getId(), auctionId)) {
            throw new ApiException(400, "위시리스트 추가 실패", "이미 위시리스트에 추가한 경매입니다.");
        }
        auctionWishListRepository.save(
                AuctionWishListEntity.builder()
                        .auction(wishedAuction)
                        .user(account)
                        .build()
        );
    }

    @Transactional
    protected  void deletedWishedAuction(Account account, Long auctionId) {
        Auction unwishedAuction = auctionRepository.findById(auctionId).orElseThrow(NotFoundAuctionException::new);
        if (!auctionWishListRepository.existsByUserIdAndAuctionId(account.getId(), auctionId)) {
            throw new ApiException(400, "위시리스트 삭제 실패", "위시리스트에 추가된 경매가 아닙니다");
        }
        auctionWishListRepository.deleteByUserIdAndAuctionId(account.getId(), auctionId);

    }
}
