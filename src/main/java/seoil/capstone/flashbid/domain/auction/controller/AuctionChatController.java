package seoil.capstone.flashbid.domain.auction.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import seoil.capstone.flashbid.domain.auction.entity.AuctionChatEntity;
import seoil.capstone.flashbid.domain.auction.repository.AuctionChatRepository;
import seoil.capstone.flashbid.domain.auction.controller.swagger.AuctionChatSwagger;
import seoil.capstone.flashbid.global.common.response.ApiResult;

import java.util.List;


@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/auction/chat")
public class AuctionChatController implements AuctionChatSwagger {
    private final AuctionChatRepository auctionChatRepository;


    @Override
    @GetMapping("/{id}")
    public ApiResult<List<AuctionChatEntity>> getAllChatList(
            @PathVariable(name = "id") Long auctionId,
            HttpServletRequest request
    ){
        return  ApiResult.ok(auctionChatRepository.findAllByAuctionId(auctionId),request,"채팅 내역 조회 성공");
    }
}
