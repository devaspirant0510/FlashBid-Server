package com.choing.flashbid.domain.auction.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.*;
import com.choing.flashbid.domain.auction.projection.AuctionChatProjection;
import com.choing.flashbid.domain.auction.service.AuctionChatService;
import com.choing.flashbid.global.common.response.ApiResult;


@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v2/auction/chat")
public class AuctionChatV2Controller {
    private final AuctionChatService auctionChatService;

    //    @GetMapping("/{id}")
//    public ApiResult<List<AuctionChatProjection>> getAllChatList(@PathVariable(name = "id") Long auctionId){
//        return ApiResult.ok(auctionChatService.findAllAuctionChatByAuctionId(auctionId),"채팅 내역 조회 성공");
//    }
    @GetMapping("/{id}")
    public ApiResult<Slice<AuctionChatProjection>> getAllChatList(
            @PathVariable(name = "id") Long auctionId,
            @RequestParam(name = "cursor",required = false) Long cursorId,
            @RequestParam(name = "size",defaultValue = "10") Integer size
    ) {
        log.info("cusor id ,{}",cursorId);
        return ApiResult.ok(auctionChatService.findAllAuctionChayByAuctionIdWithCursor(auctionId,cursorId,size), "채팅 내역 조회 성공");
    }
}
