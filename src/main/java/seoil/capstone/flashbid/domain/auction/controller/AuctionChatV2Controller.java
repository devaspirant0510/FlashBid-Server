package seoil.capstone.flashbid.domain.auction.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import seoil.capstone.flashbid.domain.auction.projection.AuctionChatProjection;
import seoil.capstone.flashbid.domain.auction.service.AuctionChatService;
import seoil.capstone.flashbid.global.common.response.ApiResult;

import java.util.List;


@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v2/auction/chat")
public class AuctionChatV2Controller {
    private final AuctionChatService auctionChatService;
    @GetMapping("/{id}")
    public ApiResult<List<AuctionChatProjection>> getAllChatList(@PathVariable(name = "id") Long auctionId){
        return ApiResult.ok(auctionChatService.findAllAuctionChatByAuctionId(auctionId),"채팅 내역 조회 성공");

    }
}
