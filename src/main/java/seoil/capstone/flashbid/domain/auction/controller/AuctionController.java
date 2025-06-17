package seoil.capstone.flashbid.domain.auction.controller;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import seoil.capstone.flashbid.domain.auction.dto.request.CreateAuctionRequestDto;
import seoil.capstone.flashbid.domain.auction.dto.request.ParticipateAuctionDto;
import seoil.capstone.flashbid.domain.auction.dto.response.AuctionDto;
import seoil.capstone.flashbid.domain.auction.entity.Auction;
import seoil.capstone.flashbid.domain.auction.repository.AuctionRepository;
import seoil.capstone.flashbid.domain.auction.service.AuctionService;
import seoil.capstone.flashbid.domain.user.entity.Account;
import seoil.capstone.flashbid.global.aop.annotation.AuthUser;
import seoil.capstone.flashbid.global.common.enums.AuctionType;
import seoil.capstone.flashbid.global.common.response.ApiResult;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/auction")
@Slf4j
public class AuctionController {
    private final AuctionService auctionService;
    private final AuctionRepository auctionRepository;

    @PostMapping("/live")
    @AuthUser
    public ApiResult<Auction> createLiveAuction(
            @Parameter(hidden = true) Account account,
            @RequestParam("files") List<MultipartFile> files,
            @RequestPart("data") CreateAuctionRequestDto dto,
            HttpServletRequest request
    ) {
        Auction auction = auctionService.saveAuction(account, dto, files, AuctionType.LIVE);
        return ApiResult.created(auction, request);
    }

    @GetMapping("/hot")
    public ApiResult<List<AuctionDto>> getAllRecommendAuction(HttpServletRequest request) {
        return ApiResult.ok(auctionService.getRecomendAuction(), request, "hot 옥션 조회 성공");
    }

    @GetMapping("/{id}")
    public ApiResult<AuctionDto> getAuctionById(
            @PathVariable(name = "id") Long auctionId,
            HttpServletRequest request
    ) {
        return ApiResult.ok(auctionService.getAuctionById(auctionId), request);
    }

    @PostMapping("/participate")
    @AuthUser
    public ApiResult<?> participateAuction(Account user, @RequestBody ParticipateAuctionDto dto, HttpServletRequest request) {
        return ApiResult.created(auctionService.participateUser(user, dto), request, "성공적으로 경매장에 참가하였습니다.");
    }


    @PostMapping("/blind")
    @AuthUser
    public ApiResult<Auction> createBlindAuction(
            Account account,
            @RequestParam("files") List<MultipartFile> files,
            @RequestPart("data") CreateAuctionRequestDto dto,
            HttpServletRequest request
    ) {
        Auction auction = auctionService.saveAuction(account, dto, files, AuctionType.BLIND);
        return ApiResult.created(auction, request);
    }


    @GetMapping("/test/all")
    public ApiResult<List<AuctionDto>> getAllTestAuction(HttpServletRequest request) {
        return ApiResult.ok(auctionService.queryAllAuction(), request);
    }


}
