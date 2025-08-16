package seoil.capstone.flashbid.domain.auction.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import seoil.capstone.flashbid.domain.auction.dto.request.CreateAuctionRequestDto;
import seoil.capstone.flashbid.domain.auction.dto.request.ParticipateAuctionDto;
import seoil.capstone.flashbid.domain.auction.dto.response.AuctionDto;
import seoil.capstone.flashbid.domain.auction.dto.response.AuctionInfoDto;
import seoil.capstone.flashbid.domain.auction.dto.response.GoodsDto;
import seoil.capstone.flashbid.domain.auction.entity.*;
import seoil.capstone.flashbid.domain.auction.projection.BidLoggingChartProjection;
import seoil.capstone.flashbid.domain.auction.projection.BidLoggingProjection;
import seoil.capstone.flashbid.domain.auction.repository.*;
import seoil.capstone.flashbid.domain.category.entity.CategoryEntity;
import seoil.capstone.flashbid.domain.category.repository.CategoryRepository;
import seoil.capstone.flashbid.domain.file.entity.FileEntity;
import seoil.capstone.flashbid.domain.file.service.FileService;
import seoil.capstone.flashbid.domain.user.entity.Account;
import seoil.capstone.flashbid.global.common.enums.AuctionType;
import seoil.capstone.flashbid.global.common.enums.DeliveryType;
import seoil.capstone.flashbid.global.common.enums.FileType;
import seoil.capstone.flashbid.global.common.error.ApiException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuctionService {
    private final FileService fileService;
    private final GoodsService goodsService;
    private final AuctionRepository auctionRepository;
    private final AuctionParticipateRepository auctionParticipateRepository;
    private final AuctionBidLogRepository auctionBidLogRepository;
    private final CategoryRepository categoryRepository;

    private final DeliveryInfoRepository deliveryInfoRepository;
    private final TradingAreaRepository tradingAreaRepository;
    private final ConfirmedBidsRepository confirmedBidsRepository;
    private final AuctionChatRepository auctionChatRepository;
    private final AuctionWishListRepository auctionWishListRepository;
    private final AuctionWishListCountRepository auctionWishListCountRepository;

    public ConfirmedBidsEntity confirmedBidsEntity(Account account, Long auctionId, Long biddingLogId) {
        Auction auction = getAuctionById(auctionId);
        BiddingLogEntity bidding = auctionBidLogRepository.findById(biddingLogId).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "", ""));
        ConfirmedBidsEntity build = ConfirmedBidsEntity.builder()
                .bidder(account)
                .biddingLog(bidding)
                .auction(auction)
                .seller(auction.getUser())
                .build();
        build.setCreatedAt(LocalDateTime.now());
        confirmedBidsRepository.save(build);
        return build;

    }

    public Auction getAuctionById(Long auctionId) {
        return auctionRepository.findById(auctionId).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "", ""));
    }

    @Transactional
    public Auction saveAuction(Account user, CreateAuctionRequestDto dto, List<MultipartFile> images, AuctionType auctionType) {
        GoodsDto goodsDto = goodsService.uploadGoods(user, images, dto.getTitle(), dto.getDescription(), dto.getDeliveryType());
        CategoryEntity category = categoryRepository.findById(dto.getCategoryId()).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "", ""));
        Auction auction = Auction.builder()
                .count(0)
                .bidUnit(dto.getBidUnit())
                .endTime(dto.getEndTime())
                .auctionType(auctionType)
                .startTime(dto.getStartTime())
                .startPrice(dto.getStartPrice())
                .goods(goodsDto.getGoods())
                .category(category)
                .user(user)
                .viewCount(0)
                .build();

        Auction savedAuction = auctionRepository.save(auction);

        if (dto.getDeliveryType() == DeliveryType.PARCEL && dto.getDeliveryInfo() != null) {
            DeliveryInfoEntity deliveryInfo = DeliveryInfoEntity.builder()
                    .deliveryFee(dto.getDeliveryInfo().getDeliveryFee())
                    .build();
            DeliveryInfoEntity save = deliveryInfoRepository.saveAndFlush(deliveryInfo);
            log.info("saved {}", save);
            savedAuction.setDeliveryInfo(save);
        } else if (dto.getDeliveryType() == DeliveryType.DIRECT && dto.getTradingArea() != null) {
            TradingAreaEntity tradingArea = TradingAreaEntity.builder()
                    .latitude(dto.getTradingArea().getLatitude())
                    .longitude(dto.getTradingArea().getLongitude())
                    .radius(dto.getTradingArea().getRadius())
                    .address(dto.getTradingArea().getAddress())
                    .build();
            TradingAreaEntity save = tradingAreaRepository.saveAndFlush(tradingArea);
            savedAuction.setTradingArea(save);
        }

        return savedAuction;
    }

    @Transactional
    public AuctionInfoDto getAuctionInfoByIdToDto(Long id) {
        Auction auction = auctionRepository.findById(id).orElseThrow(() ->
                new ApiException(HttpStatus.NOT_FOUND, "", "")
        );
        List<FileEntity> allFiles = fileService.getAllFiles(auction.getGoods().getId(), FileType.GOODS);
        BiddingLogEntity bidHistory = auctionBidLogRepository.findTop1ByAuctionIdOrderByCreatedAtDesc(id);
        Long biddingCount = auctionBidLogRepository.countByAuctionId(id);
        // 찜 목록 카운트
        AuctionWishListCountEntity wishListCountEntity = auctionWishListCountRepository.findById(id)
                .orElse(AuctionWishListCountEntity.builder().count(0L).build());
        Long wishListCount = wishListCountEntity.getCount();
        // 찜 여부 확인
        boolean isWishListed = auctionWishListRepository.existsByUserIdAndAuctionId(auction.getUser().getId(), id);
        return new AuctionInfoDto(
                auction,
                allFiles,
                auctionParticipateRepository.countByAuctionId(id),
                biddingCount,
                bidHistory,
                wishListCount,
                isWishListed
        );
    }


    public List<AuctionDto> getRecomendAuction() {
        List<AuctionDto> auctionDtos = new ArrayList<>();
        auctionRepository.findTop4ByOrderByCreatedAtDesc().forEach(auction -> {
            BiddingLogEntity bidHistory = auctionBidLogRepository.findTop1ByAuctionIdOrderByCreatedAtDesc(auction.getId());
            Long chatCount = auctionChatRepository.countByAuctionId(auction.getId());
            // 찜 목록 카운트
            AuctionWishListCountEntity wishListCountEntity = auctionWishListCountRepository.findById(auction.getId())
                    .orElse(AuctionWishListCountEntity.builder().count(0L).build());
            Long wishListCount = wishListCountEntity.getCount();
            // 경매 DTO 생성
            auctionDtos.add(new AuctionDto(auction, fileService.getAllFiles(auction.getGoods().getId(), FileType.GOODS),
                    auctionParticipateRepository.countByAuctionId(auction.getId()),
                    bidHistory != null ? bidHistory.getPrice() : null,
                    chatCount,
                    wishListCount
            ));

        });
        return auctionDtos;
    }

    @Transactional
    public List<AuctionDto> queryAllAuction() {
        List<AuctionDto> auctionDtos = new ArrayList<>();
        auctionRepository.findAllByOrderByCreatedAtDesc().forEach(auction -> {

            BiddingLogEntity bidHistory = auctionBidLogRepository.findTop1ByAuctionIdOrderByCreatedAtDesc(auction.getId());
            Long chatCount = auctionChatRepository.countByAuctionId(auction.getId());
            // 찜 목록 카운트
            AuctionWishListCountEntity wishListCountEntity = auctionWishListCountRepository.findById(auction.getId())
                    .orElse(AuctionWishListCountEntity.builder().count(0L).build());
            Long wishListCount = wishListCountEntity.getCount();
            auctionDtos.add(new AuctionDto(auction, fileService.getAllFiles(auction.getGoods().getId(), FileType.GOODS),
                    auctionParticipateRepository.countByAuctionId(auction.getId()),
                    bidHistory != null ? bidHistory.getPrice() : null,
                    chatCount,
                    wishListCount
            ));
        });
        return auctionDtos;
    }


    public AuctionParticipateEntity participateUser(Account user, ParticipateAuctionDto dto) {
        //TODO: 경매시간이 유효한지 검사
        //TODO : 이미 가입했는지 검사
        //TODO : 판매자가 본인상품에 요청했는지 검사
        Auction auction = auctionRepository.findById(dto.getAuctionId()).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "", ""));
        AuctionParticipateEntity participate = AuctionParticipateEntity.builder()
                .auction(auction)
                .participant(user)
                .build();
        return auctionParticipateRepository.save(participate);
    }

    public List<BidLoggingProjection> findAllBidLogForAccountId(Long auctionId, Pageable pageable) {
        return auctionBidLogRepository.findAllBidLogHistoryByAuctionId(auctionId, pageable);
    }

    public List<BidLoggingChartProjection> findAllBidLogChartData(Long auctionId) {
        return auctionBidLogRepository.findAllByAuctionIdOrderByCreatedAtAsc(auctionId);
    }

    // 좋아요 로직 구현
    @Transactional
    public AuctionWishListEntity addWishList(Account user, Long auctionId) {
        // 경매가 존재하는지 확인
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Auction not found", "해당 경매를 찾을 수 없습니다."));
        // 이미 찜한 경매인지 확인
        boolean isAddedWishList = auctionWishListRepository.existsByUserIdAndAuctionId(user.getId(), auction.getId());
        if (isAddedWishList) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "이미 찜한 경매입니다.", "이미 찜한 경매입니다.");
        }
        // 찜 목록에 추가
        AuctionWishListEntity wishList = AuctionWishListEntity.builder()
                .user(user)
                .auction(auction)
                .build();
        AuctionWishListEntity savedWishList = auctionWishListRepository.save(wishList);
        AuctionWishListCountEntity countEntity = auctionWishListCountRepository.findById(auctionId).orElse(null);
        // 찜 카운트 업데이트
        if (countEntity != null) {
            countEntity.setCount(countEntity.getCount() + 1);
            auctionWishListCountRepository.save(countEntity);

        } else {
            // 찜 카운트가 없으면 새로 생성
            AuctionWishListCountEntity newCountEntity = AuctionWishListCountEntity.builder()
                    .auction(auction)
                    .count(1L)
                    .build();
            auctionWishListCountRepository.save(newCountEntity);
        }
        return savedWishList;

    }

    // 좋아요 취소 로직 구현
    @Transactional
    public void removeWishList(Account user, Long auctionId) {
        // 경매가 존재하는지 확인
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Auction not found", "해당 경매를 찾을 수 없습니다."));
        // 찜 목록에서 제거
        auctionWishListRepository.findByUserIdAndAuctionId(user.getId(), auction.getId())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "WishList not found", "찜 목록에 해당 경매가 없습니다."));
        auctionWishListRepository.deleteByUserIdAndAuctionId(user.getId(), auction.getId());

    }

}
