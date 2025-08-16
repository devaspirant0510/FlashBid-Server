package seoil.capstone.flashbid.domain.auction.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import seoil.capstone.flashbid.domain.auction.controller.swagger.AuctionSwagger;
import seoil.capstone.flashbid.domain.auction.dto.request.CreateAuctionRequestDto;
import seoil.capstone.flashbid.domain.auction.dto.request.CreateConfirmRequestDto;
import seoil.capstone.flashbid.domain.auction.dto.request.ParticipateAuctionDto;
import seoil.capstone.flashbid.domain.auction.dto.response.AuctionDto;
import seoil.capstone.flashbid.domain.auction.dto.response.AuctionInfoDto;
import seoil.capstone.flashbid.domain.auction.entity.Auction;
import seoil.capstone.flashbid.domain.auction.entity.ConfirmedBidsEntity;
import seoil.capstone.flashbid.domain.auction.projection.AuctionParticipantsProjection;
import seoil.capstone.flashbid.domain.auction.projection.BidLoggingChartProjection;
import seoil.capstone.flashbid.domain.auction.projection.BidLoggingProjection;
import seoil.capstone.flashbid.domain.auction.repository.AuctionBidLogRepository;
import seoil.capstone.flashbid.domain.auction.repository.AuctionParticipateRepository;
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
public class AuctionController implements AuctionSwagger {
    private final AuctionService auctionService;
    private final AuctionParticipateRepository auctionParticipateRepository;
    private final AuctionBidLogRepository auctionBidLogRepository;

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
        return ApiResult.ok(confirmedBidsEntity, request);
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
        return ApiResult.created(auction, request);
    }

    @Override
    @GetMapping("/hot")
    public ApiResult<List<AuctionDto>> getAllRecommendAuction(HttpServletRequest request) {
        return ApiResult.ok(auctionService.getRecomendAuction(), request, "hot 옥션 조회 성공");
    }

    @Override
    @GetMapping("/{id}")
    public ApiResult<AuctionInfoDto> getAuctionById(
            @PathVariable(name = "id") Long auctionId,
            HttpServletRequest request
    ) {
        return ApiResult.ok(auctionService.getAuctionInfoByIdToDto(auctionId), request);
    }

    @Override
    @PostMapping("/participate")
    @AuthUser
    public ApiResult<?> participateAuction(Account user, @RequestBody ParticipateAuctionDto dto, HttpServletRequest request) {
        return ApiResult.created(auctionService.participateUser(user, dto), request, "성공적으로 경매장에 참가하였습니다.");
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
        return ApiResult.created(auction, request);
    }

    @Override
    @GetMapping("/test/all")
    public ApiResult<List<AuctionDto>> getAllTestAuction(HttpServletRequest request) {
        return ApiResult.ok(auctionService.queryAllAuction(), request);
    }

    @Override
    @GetMapping("/bid-history/{id}")
    public ApiResult<List<BidLoggingProjection>> getBidHistoryByAuctionId(
            @PathVariable("id")
            Long auctionId,
            HttpServletRequest request,
            Pageable pageable
    ) {
        return ApiResult.ok(auctionService.findAllBidLogForAccountId(auctionId, pageable), request);
    }

    @Override
    @GetMapping("/bid-history/{id}/page")
    public ApiResult<Page<List<BidLoggingProjection>>> getBidHistoryByAuctionIdWithPage(@PathVariable(name = "id") Long auctionId, HttpServletRequest request, Pageable pageable) {
        return ApiResult.ok(auctionBidLogRepository.findAllBidLogHistoryByAuctionIdWithPage(auctionId, pageable), request);
    }

    @Override
    @GetMapping("/bid-history/chart/{id}")
    public ApiResult<List<BidLoggingChartProjection>> getBidHistoryByAuctionIdWithChart(
            @PathVariable("id")
            Long auctionId,
            HttpServletRequest request
    ) {
        return ApiResult.ok(auctionService.findAllBidLogChartData(auctionId), request);
    }

    @Override
    @GetMapping("/auction/{id}/participant-user")
    public ApiResult<Page<AuctionParticipantsProjection>> getAuctionParticipantsByAuctionId(@PathVariable("id") Long auctionId, HttpServletRequest request, Pageable pageable) {
        return ApiResult.ok(auctionParticipateRepository.findAllByAuctionId(auctionId, pageable), request);
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
        return ApiResult.ok(true, request, "경매 찜하기 성공");
    }

    @Override
    @AuthUser
    @DeleteMapping("/wishlist/{id}")
    public ApiResult<Boolean> cancelWishAuction(Account user, @PathVariable("id") Long auctionId, HttpServletRequest request) {
        auctionService.removeWishList(user, auctionId);
        return ApiResult.ok(true, request, "경매 찜하기 취소 성공");
    }
}
