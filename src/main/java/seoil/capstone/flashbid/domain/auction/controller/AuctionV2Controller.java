package seoil.capstone.flashbid.domain.auction.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import seoil.capstone.flashbid.domain.auction.projection.AuctionProjection;
import seoil.capstone.flashbid.domain.auction.service.AuctionService;
import seoil.capstone.flashbid.global.common.response.ApiResult;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/v2/auction")
@Slf4j
public class AuctionV2Controller {
    private final AuctionService auctionService;

    @GetMapping("live")
    public ApiResult<Page<AuctionProjection>> getAuctionList(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @RequestParam(name = "category", required = false) String categoryName
    ){
        Page<AuctionProjection> auctionProjections = auctionService.searchAuction(
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


}
