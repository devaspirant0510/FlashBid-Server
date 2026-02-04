package seoil.capstone.flashbid.domain.auction.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import seoil.capstone.flashbid.domain.auction.dto.response.AuctionDetailDto;
import seoil.capstone.flashbid.domain.auction.dto.response.AuctionItemDto;
import seoil.capstone.flashbid.domain.auction.service.AuctionService;
import seoil.capstone.flashbid.domain.user.entity.Account;
import seoil.capstone.flashbid.global.aop.annotation.AuthUser;
import seoil.capstone.flashbid.global.common.response.ApiResult;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/v2/auction")
@Slf4j
public class AuctionV2Controller {
    private final AuctionService auctionService;

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


}
