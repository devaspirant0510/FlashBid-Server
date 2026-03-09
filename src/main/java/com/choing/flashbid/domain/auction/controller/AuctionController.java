package com.choing.flashbid.domain.auction.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.choing.flashbid.domain.auction.controller.swagger.AuctionSwagger;
import com.choing.flashbid.domain.auction.dto.request.CreateAuctionRequestDto;
import com.choing.flashbid.domain.auction.dto.request.CreateConfirmRequestDto;
import com.choing.flashbid.domain.auction.dto.request.ParticipateAuctionDto;
import com.choing.flashbid.domain.auction.dto.response.AuctionDto;
import com.choing.flashbid.domain.auction.dto.response.AuctionInfoDto;
import com.choing.flashbid.domain.auction.dto.response.ViewCountIncreasedDto;
import com.choing.flashbid.domain.auction.dto.response.ViewCountResultDto;
import com.choing.flashbid.domain.auction.entity.Auction;
import com.choing.flashbid.domain.auction.entity.ConfirmedBidsEntity;
import com.choing.flashbid.domain.auction.projection.*;
import com.choing.flashbid.domain.auction.repository.jpa.AuctionBidLogRepository;
import com.choing.flashbid.domain.auction.repository.jpa.AuctionParticipateRepository;
import com.choing.flashbid.domain.auction.service.AuctionService;
import com.choing.flashbid.domain.auction.service.AuctionViewService;
import com.choing.flashbid.domain.payment.dto.BidDto;
import com.choing.flashbid.domain.user.entity.Account;
import com.choing.flashbid.global.aop.annotation.AuthUser;
import com.choing.flashbid.global.common.enums.AuctionType;
import com.choing.flashbid.global.common.response.ApiResult;
import com.choing.flashbid.global.core.provider.ClientIdentifierProvider;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/auction")
@Slf4j
public class AuctionController implements AuctionSwagger {
    private final AuctionService auctionService;
    private final AuctionViewService auctionViewService;
    private final AuctionParticipateRepository auctionParticipateRepository;
    private final AuctionBidLogRepository auctionBidLogRepository;
    private final ClientIdentifierProvider clientIdentifierProvider;

    @Override
    @PostMapping("/confirm/{id}")
    @AuthUser
    public ApiResult<ConfirmedBidsEntity> confirmBids(
            @PathVariable("id") Long auctionId,
            Account user,
            @RequestBody CreateConfirmRequestDto dto,
            HttpServletRequest request
    ) {
        ConfirmedBidsEntity confirmedBidsEntity = auctionService.confirmedBidsEntity(user, dto.getAuctionId(), dto.getBiddingLogId());
        return ApiResult.ok(confirmedBidsEntity);
    }

    @Override
    @PostMapping("/live")
    @AuthUser
    public ApiResult<Auction> createLiveAuction(
            Account account,
            @RequestParam("files") List<MultipartFile> files,
            @RequestPart("data") CreateAuctionRequestDto dto,
            HttpServletRequest request
    ) {
        log.info(dto.toString());
        Auction auction = auctionService.saveAuction(account, dto, files, AuctionType.LIVE);
        return ApiResult.created(auction);
    }

    @Override
    @GetMapping("/hot")
    public ApiResult<List<AuctionDto>> getAllRecommendAuction(HttpServletRequest request) {
        return ApiResult.ok(auctionService.getRecomendAuction(), "hot 옥션 조회 성공");
    }

    @GetMapping("/recommend/{id}")
    public ApiResult<List<AuctionDto>> getRecommendAuctionById(
            @PathVariable("id") Long auctionId,
            HttpServletRequest request
    ) {
        return ApiResult.ok(auctionService.getRecommendAuction(auctionId), "추천 경매 조회 성공");
    }

    @Override
    @GetMapping("/{id}")
    @AuthUser
    public ApiResult<AuctionInfoDto> getAuctionById(
            @PathVariable(name = "id") Long auctionId,
            Account user,
            HttpServletRequest request
    ) {
        return ApiResult.ok(auctionService.getAuctionInfoByIdToDto(auctionId, user == null ? null : user.getId()));
    }

    @Override
    @PostMapping("/participate")
    @AuthUser
    public ApiResult<?> participateAuction(Account user, @RequestBody ParticipateAuctionDto dto, HttpServletRequest request) {
        return ApiResult.created(auctionService.participateUser(user, dto), "성공적으로 경매장에 참가하였습니다.");
    }

    @Override
    @PostMapping("/blind")
    @AuthUser
    public ApiResult<Auction> createBlindAuction(
            Account account,
            @RequestParam("files") List<MultipartFile> files,
            @RequestPart("data") CreateAuctionRequestDto dto,
            HttpServletRequest request
    ) {
        Auction auction = auctionService.saveAuction(account, dto, files, AuctionType.BLIND);
        return ApiResult.created(auction);
    }

    @Override
    @GetMapping("/test/all/live")
    public ApiResult<List<AuctionDto>> getAllTestLiveAuction(HttpServletRequest request) {
        return ApiResult.ok(auctionService.queryAllAuction(AuctionType.LIVE));
    }

    @GetMapping("/live")
    public ApiResult<Slice<AuctionProjection>> getAllLiveAuctionPage(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @RequestParam(name = "category", required = false) String categoryName
    ) {
        return ApiResult.ok(auctionService.queryGetAllAuction(AuctionType.LIVE, categoryName, page - 1, size), "라이브 옥션 페이지 조회 성공");
    }

    @GetMapping("/blind")
    public ApiResult<Slice<AuctionProjection>> getAllBlindAuctionPage(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @RequestParam(name = "category", required = false) String categoryName
    ) {
        return ApiResult.ok(auctionService.queryGetAllAuction(AuctionType.BLIND, categoryName, page - 1, size), "라이브 옥션 페이지 조회 성공");
    }

    @Override
    @GetMapping("/test/all/blind")
    public ApiResult<List<AuctionDto>> getAllTestBlindAuction(HttpServletRequest request) {
        return ApiResult.ok(auctionService.queryAllAuction(AuctionType.BLIND));
    }

    @Override
    @GetMapping("/bid-history/{id}")
    public ApiResult<List<BidLoggingProjection>> getBidHistoryByAuctionId(
            @PathVariable("id")
            Long auctionId,
            HttpServletRequest request,
            Pageable pageable
    ) {
        return ApiResult.ok(auctionService.findAllBidLogForAccountId(auctionId, pageable));
    }

    @GetMapping("/bid-history/{id}/daily-summary")
    public ApiResult<List<BiddingLogDailySummary>> getBidHistoryDailySummaryByAuctionId(
            @PathVariable("id")
            Long auctionId
    ) {
        return ApiResult.ok(auctionService.findBidLogDailySummaryByAuctionId(auctionId));
    }

    @Override
    @GetMapping("/bid-history/{id}/page")
    public ApiResult<Page<List<BidLoggingProjection>>> getBidHistoryByAuctionIdWithPage(@PathVariable(name = "id") Long auctionId, HttpServletRequest request, Pageable pageable) {
        return ApiResult.ok(auctionBidLogRepository.findAllBidLogHistoryByAuctionIdWithPage(auctionId, pageable));
    }

    @Override
    @GetMapping("/bid-history/chart/{id}")
    public ApiResult<List<BidLoggingChartProjection>> getBidHistoryByAuctionIdWithChart(
            @PathVariable("id")
            Long auctionId,
            HttpServletRequest request
    ) {
        return ApiResult.ok(auctionService.findAllBidLogChartData(auctionId));
    }

    @Override
    @GetMapping("/auction/{id}/participant-user")
    public ApiResult<Page<AuctionParticipantsProjection>> getAuctionParticipantsByAuctionId(@PathVariable("id") Long auctionId, HttpServletRequest request, Pageable pageable) {
        return ApiResult.ok(auctionParticipateRepository.findAllByAuctionId(auctionId, pageable));
    }

    @Override
    public ApiResult<List<AuctionDto>> getRecommendAuction(Long currentAuctionId, HttpServletRequest request) {
        return null;
    }


    @Override
    @AuthUser
    @PatchMapping("/wishlist/{id}")
    public ApiResult<Boolean> wishAuction(Account user, @PathVariable("id") Long auctionId, HttpServletRequest request) {
        auctionService.addWishList(user, auctionId);
        return ApiResult.ok(true, "경매 찜하기 성공");
    }

    @Override
    @AuthUser
    @DeleteMapping("/wishlist/{id}")
    public ApiResult<Boolean> cancelWishAuction(Account user, @PathVariable("id") Long auctionId, HttpServletRequest request) {
        auctionService.removeWishList(user, auctionId);
        return ApiResult.ok(true, "경매 찜하기 취소 성공");
    }

    @PostMapping("/payment")
    @AuthUser
    public void paymentAuctionBid(Account user, @RequestBody BidDto dto) {
        auctionService.paymentAuctionBid(user, dto);

    }

    @GetMapping("/confirmed/{id}")
    public ApiResult<ConfirmedBidsEntity> getConfirmedBids(@PathVariable("id") Long auctionId) {
        ConfirmedBidsEntity confirmedBidsEntity = auctionService.getConfirmedBids(auctionId);
        return ApiResult.ok(confirmedBidsEntity, confirmedBidsEntity != null ? "낙찰 정보 조회 성공" : "낙찰 정보 없음");
    }

    @PatchMapping("/views/{id}")
    @Deprecated
    public ApiResult<Boolean> updateAuctionViews(@PathVariable("id") Long auctionId) {
        auctionService.updateAuctionViews(auctionId);
        return ApiResult.ok(true, "경매 조회수 증가 성공");
    }

    @PatchMapping("{id}/views")
    @AuthUser
    public ApiResult<ViewCountIncreasedDto> updateAuctionViews(
            @PathVariable("id") Long auctionId,
            Account account,
            HttpServletRequest request
    ) {
        return ApiResult.ok(
                auctionViewService.increaseView(
                        auctionId,
                        account,
                        request
                )
        );
    }


    @GetMapping("{id}/views/count")
    public ApiResult<ViewCountResultDto> getViewCount(
            @PathVariable(name = "id") Long auctionId
    ) {
        return ApiResult.ok(
                auctionViewService.getAuctionViewCount(auctionId)
        );


    }

}
