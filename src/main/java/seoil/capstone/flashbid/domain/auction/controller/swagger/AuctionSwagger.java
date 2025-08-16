package seoil.capstone.flashbid.domain.auction.controller.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import seoil.capstone.flashbid.domain.auction.dto.response.AuctionDto;
import seoil.capstone.flashbid.domain.auction.projection.AuctionParticipantsProjection;
import seoil.capstone.flashbid.domain.auction.projection.BidLoggingChartProjection;
import seoil.capstone.flashbid.domain.auction.projection.BidLoggingProjection;
import seoil.capstone.flashbid.domain.auction.dto.request.CreateAuctionRequestDto;
import seoil.capstone.flashbid.domain.auction.dto.request.CreateConfirmRequestDto;
import seoil.capstone.flashbid.domain.auction.dto.request.ParticipateAuctionDto;
import seoil.capstone.flashbid.domain.auction.entity.Auction;
import seoil.capstone.flashbid.domain.auction.entity.ConfirmedBidsEntity;
import seoil.capstone.flashbid.domain.user.entity.Account;
import seoil.capstone.flashbid.global.common.response.ApiResult;

import java.util.List;

@Tag(name = "Auction API", description = "경매 관련 API")
public interface AuctionSwagger {
    @Operation(summary = "경매 낙찰", description = "경매를 낙찰합니다.")
    ApiResult<ConfirmedBidsEntity> confirmBids(
            @Parameter(description = "경매 ID") @PathVariable("id") Long auctionId,
            @Parameter(hidden = true) Account user,
            @RequestBody CreateConfirmRequestDto dto,
            HttpServletRequest request
    );

    @Operation(summary = "라이브 경매 생성", description = "라이브 경매를 생성합니다.")
    ApiResult<Auction> createLiveAuction(
            @Parameter(hidden = true) Account account,
            @Parameter(description = "이미지 파일") @RequestParam("files") List<MultipartFile> files,
            @Parameter(description = "경매 정보") @RequestPart("data") CreateAuctionRequestDto dto,
            HttpServletRequest request
    );

    @Operation(summary = "추천 경매 조회", description = "추천 경매 목록을 조회합니다.")
    ApiResult<?> getAllRecommendAuction(HttpServletRequest request);

    @Operation(summary = "경매 상세 조회", description = "경매 ID로 경매 상세 정보를 조회합니다.")
    ApiResult<?> getAuctionById(
            @Parameter(description = "경매 ID") @PathVariable(name = "id") Long auctionId,
            Account user,
            HttpServletRequest request
    );

    @Operation(summary = "경매 참가", description = "경매에 참가합니다.")
    ApiResult<?> participateAuction(
            @Parameter(hidden = true) Account user,
            @RequestBody ParticipateAuctionDto dto,
            HttpServletRequest request
    );

    @Operation(summary = "비공개 경매 생성", description = "비공개 경매를 생성합니다.")
    ApiResult<Auction> createBlindAuction(
            @Parameter(hidden = true) Account account,
            @Parameter(description = "이미지 파일") @RequestParam("files") List<MultipartFile> files,
            @Parameter(description = "경매 정보") @RequestPart("data") CreateAuctionRequestDto dto,
            HttpServletRequest request
    );

    @Operation(summary = "테스트용 모든 경매 조회", description = "테스트용으로 모든 경매 목록을 조회합니다.")
    ApiResult<?> getAllTestAuction(HttpServletRequest request);

    @Operation(summary = "경매 내역 조회", description = "테스트용으로 모든 경매 목록을 조회합니다.")
    ApiResult<List<BidLoggingProjection>> getBidHistoryByAuctionId(Long auctionId, HttpServletRequest request, Pageable pageable);

    @Operation(summary = "경매 내역 조회", description = "테스트용으로 모든 경매 목록을 조회합니다.")
    ApiResult<Page<List<BidLoggingProjection>>> getBidHistoryByAuctionIdWithPage(Long auctionId, HttpServletRequest request, Pageable pageable);

    @Operation(summary = "경매 내역 차트 조회", description = "테스트용으로 모든 경매 목록을 조회합니다.")
    ApiResult<List<BidLoggingChartProjection>> getBidHistoryByAuctionIdWithChart(Long auctionId, HttpServletRequest request);

    @Operation(summary = "경매 참가자 목록 조회", description = "해당 경매 참가자 목록을 조회 합니다.")
    ApiResult<Page<AuctionParticipantsProjection>> getAuctionParticipantsByAuctionId(Long auctionId, HttpServletRequest request, Pageable pageable);

    @Operation(summary = "경매 참가자 목록 조회", description = "해당 경매 참가자 목록을 조회 합니다.")
    ApiResult<List<AuctionDto>> getRecommendAuction(Long currentAuctionId,HttpServletRequest request);

    @Operation(summary = "경매 찜하기", description = "경매를 찜합니다.")
    ApiResult<Boolean> wishAuction(
            @Parameter(hidden = true) Account user,
            Long auctionId,
            HttpServletRequest request
    );

    @Operation(summary = "경매 찜 취소", description = "경매를 찜한걸 취소합니다.")
    ApiResult<Boolean> cancelWishAuction(
            @Parameter(hidden = true) Account user,
            Long auctionId,
            HttpServletRequest request
    );
}
