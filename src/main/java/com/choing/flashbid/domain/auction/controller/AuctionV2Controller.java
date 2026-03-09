package com.choing.flashbid.domain.auction.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import com.choing.flashbid.domain.auction.dto.response.AuctionDetailDto;
import com.choing.flashbid.domain.auction.dto.response.AuctionItemDto;
import com.choing.flashbid.domain.auction.service.AuctionService;
import com.choing.flashbid.domain.auction.service.AuctionWishListService;
import com.choing.flashbid.domain.user.entity.Account;
import com.choing.flashbid.global.aop.annotation.AuthUser;
import com.choing.flashbid.global.common.response.ApiResult;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/v2/auction")
@Slf4j
public class AuctionV2Controller {
    private final AuctionService auctionService;
    private final AuctionWishListService auctionWishListService;

    @GetMapping("live")
    public ApiResult<Page<AuctionItemDto>> getAuctionList(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @RequestParam(name = "category", required = false) String categoryName
    ) {
        Page<AuctionItemDto> auctionProjections = auctionService.searchAuction(
                categoryName,
                0,
                page,
                size,
                10
        );
        return ApiResult.ok(
                auctionProjections,
                "실시간 경매 상품 조회"
        );
    }

    @GetMapping("/{id}")
    @AuthUser
    public ApiResult<AuctionDetailDto> getAuction(
            @PathVariable(name = "id") Long auctionId,
            Account account
    ) {
        return ApiResult.ok(auctionService.getAuctionDetail(auctionId, account));
    }

    @AuthUser
    @PatchMapping("/wishlist/{id}")
    public ApiResult<Boolean> wishAuction(Account user, @PathVariable("id") Long auctionId) {
        auctionWishListService.increase(user, auctionId);
        return ApiResult.ok(true, "경매 찜하기 성공");
    }

    @AuthUser
    @DeleteMapping("/wishlist/{id}")
    public ApiResult<Boolean> cancelWishAuction(Account user, @PathVariable("id") Long auctionId) {
        auctionWishListService.decrease(user, auctionId);
        return ApiResult.ok(true, "경매 찜하기 취소 성공");
    }


}
