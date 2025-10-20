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
import seoil.capstone.flashbid.domain.auction.projection.UserMaxBidProjection;
import seoil.capstone.flashbid.domain.auction.repository.*;
import seoil.capstone.flashbid.domain.category.entity.CategoryEntity;
import seoil.capstone.flashbid.domain.category.repository.CategoryRepository;
import seoil.capstone.flashbid.domain.dm.service.DMService;
import seoil.capstone.flashbid.domain.file.entity.FileEntity;
import seoil.capstone.flashbid.domain.file.service.FileService;
import seoil.capstone.flashbid.domain.payment.dto.BidDto;
import seoil.capstone.flashbid.domain.payment.entity.PointHistoryEntity;
import seoil.capstone.flashbid.domain.payment.repository.PointHistoryRepository;
import seoil.capstone.flashbid.domain.user.entity.Account;
import seoil.capstone.flashbid.global.common.enums.AuctionStatus;
import seoil.capstone.flashbid.global.common.enums.AuctionType;
import seoil.capstone.flashbid.global.common.enums.DeliveryType;
import seoil.capstone.flashbid.global.common.enums.FileType;
import seoil.capstone.flashbid.global.common.error.ApiException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
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
    private final PointHistoryRepository pointHistoryRepository;
    private final AuctionEventRepository auctionEventRepository;

    private final DMService dmService;

    public ConfirmedBidsEntity confirmedBidsEntity(Account account, Long auctionId, Long biddingLogId) {
        Auction auction = getAuctionById(auctionId);
        BiddingLogEntity bidding = auctionBidLogRepository.findById(biddingLogId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "", ""));

        ConfirmedBidsEntity build = ConfirmedBidsEntity.builder()
                .bidder(account)
                .biddingLog(bidding)
                .auction(auction)
                .seller(auction.getUser())
                .build();
        build.setCreatedAt(LocalDateTime.now());
        confirmedBidsRepository.save(build);

        // ✅ 수정: 경매 정보를 포함한 채팅방 생성
        dmService.createDMRoomWithAuctionInfo(
                auction.getUser(),           // 판매자
                account,                      // 구매자(낙찰자)
                auction.getGoods().getTitle() + " 거래 채팅방",
                auctionId                     // 경매 ID
        );

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
                .auctionStatus(AuctionStatus.BEFORE_START)
                .build();

        Auction savedAuction = auctionRepository.save(auction);
        auctionEventRepository.registerAuctionTTLs(
                savedAuction.getId(),
                auctionType,
                dto.getStartTime(),
                dto.getEndTime()
        );

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
    public AuctionInfoDto getAuctionInfoByIdToDto(Long id, Long userId) {
        Auction auction = auctionRepository.findById(id).orElseThrow(() ->
                new ApiException(HttpStatus.NOT_FOUND, "", "")
        );
        List<FileEntity> allFiles = fileService.getAllFiles(auction.getGoods().getId(), FileType.GOODS);
        BiddingLogEntity bidHistory = auctionBidLogRepository.findTop1ByAuctionIdOrderByPriceDesc(id);
        Long biddingCount = auctionBidLogRepository.countByAuctionId(id);
        // 찜 목록 카운트
        AuctionWishListCountEntity wishListCountEntity = auctionWishListCountRepository.findById(id)
                .orElse(AuctionWishListCountEntity.builder().count(0L).build());
        Long wishListCount = wishListCountEntity.getCount();
        // 찜 여부 확인
        boolean isWishListed = auctionWishListRepository.existsByUserIdAndAuctionId(userId, id);
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
            BiddingLogEntity bidHistory = auctionBidLogRepository.findTop1ByAuctionIdOrderByPriceDesc(auction.getId());
            Long chatCount = auctionChatRepository.countByAuctionId(auction.getId());
            // 찜 목록 카운트
            AuctionWishListCountEntity wishListCountEntity = auctionWishListCountRepository.findById(auction.getId())
                    .orElse(AuctionWishListCountEntity.builder().count(0L).build());
            Long wishListCount = wishListCountEntity.getCount();

            Long biddingCount = auctionBidLogRepository.countByAuctionId(auction.getId());
            // 경매 DTO 생성
            auctionDtos.add(new AuctionDto(auction, fileService.getAllFiles(auction.getGoods().getId(), FileType.GOODS),
                    auctionParticipateRepository.countByAuctionId(auction.getId()),
                    biddingCount,
                    bidHistory != null ? bidHistory.getPrice() : null,
                    chatCount,
                    wishListCount
            ));

        });
        return auctionDtos;
    }

    @Transactional
    public List<AuctionDto> queryAllAuction(AuctionType auctionType) {
        List<AuctionDto> auctionDtos = new ArrayList<>();
        auctionRepository.findAllByAuctionTypeAndEndTimeAfterOrderByCreatedAtDesc(auctionType, LocalDateTime.now()).forEach(auction -> {

            BiddingLogEntity bidHistory = auctionBidLogRepository.findTop1ByAuctionIdOrderByPriceDesc(auction.getId());
            Long chatCount = auctionChatRepository.countByAuctionId(auction.getId());
            // 찜 목록 카운트
            AuctionWishListCountEntity wishListCountEntity = auctionWishListCountRepository.findById(auction.getId())
                    .orElse(AuctionWishListCountEntity.builder().count(0L).build());
            Long wishListCount = wishListCountEntity.getCount();

            Long biddingCount = auctionBidLogRepository.countByAuctionId(auction.getId());
            auctionDtos.add(new AuctionDto(auction, fileService.getAllFiles(auction.getGoods().getId(), FileType.GOODS),
                    auctionParticipateRepository.countByAuctionId(auction.getId()),
                    biddingCount,
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
        // 찜 카운트 업데이트
        AuctionWishListCountEntity countEntity = auctionWishListCountRepository.findById(auctionId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "WishListCount not found", "찜 카운트가 존재하지 않습니다."));
        if (countEntity.getCount() > 0) {
            countEntity.setCount(countEntity.getCount() - 1);
            auctionWishListCountRepository.save(countEntity);
        } else {
            throw new ApiException(HttpStatus.BAD_REQUEST, "찜 카운트가 0 이하입니다.", "찜 카운트가 0 이하입니다.");
        }

    }

    @Transactional
    public List<AuctionDto> getRecommendAuction(Long currentAuctionId) {
        List<AuctionDto> auctionDtos = new ArrayList<>();
        auctionRepository.findAllByIdNot(currentAuctionId).forEach(auction -> {
            BiddingLogEntity bidHistory = auctionBidLogRepository.findTop1ByAuctionIdOrderByPriceDesc(auction.getId());
            Long chatCount = auctionChatRepository.countByAuctionId(auction.getId());
            // 찜 목록 카운트
            AuctionWishListCountEntity wishListCountEntity = auctionWishListCountRepository.findById(auction.getId())
                    .orElse(AuctionWishListCountEntity.builder().count(0L).build());
            Long wishListCount = wishListCountEntity.getCount();

            Long biddingCount = auctionBidLogRepository.countByAuctionId(auction.getId());
            // 경매 DTO 생성
            auctionDtos.add(new AuctionDto(auction, fileService.getAllFiles(auction.getGoods().getId(), FileType.GOODS),
                    auctionParticipateRepository.countByAuctionId(auction.getId()),
                    biddingCount,
                    bidHistory != null ? bidHistory.getPrice() : null,
                    chatCount,
                    wishListCount
            ));
        });
        // 리스트에서 랜덤 셔플후 랜덤으로3개 추출
        List<AuctionDto> copyDto = new ArrayList<>(auctionDtos);
        Collections.shuffle(copyDto);
        if (copyDto.size() > 3) {
            copyDto = copyDto.subList(0, 3);
        }
        return copyDto;
    }

    @Transactional
    public void paymentAuctionBid(Account user, BidDto dto) {
        if (user.getPoint() < dto.getAmount()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "포인트가 부족합니다.", "포인트가 부족합니다.");
        }
        Auction auction = auctionRepository.findById(dto.getAuctionId()).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "존재하지 않는 옥션입니다.", "해당 옥션을 찾을수 없습니다."));
        pointHistoryRepository.save(
                PointHistoryEntity.builder()
                        .earnedPoint(dto.getAmount())
                        .contents(auction.getGoods().getTitle() + " 경매 입찰")
                        .chargeType(PointHistoryEntity.ChargeType.PURCHASE)
                        .userId(user)
                        .build()
        );
        user.setPoint(user.getPoint() - dto.getAmount());
    }

    @Transactional
    public void startAuction(Long auctionId) {
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "존재하지 않는 옥션입니다.", "해당 옥션을 찾을수 없습니다."));
        auction.setAuctionStatus(AuctionStatus.IN_PROGRESS);
    }

    @Transactional
    public ConfirmedBidsEntity endAuction(Long auctionId) {
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "존재하지 않는 옥션입니다.", "해당 옥션을 찾을수 없습니다."));

        auction.setAuctionStatus(AuctionStatus.ENDED);

        // 입찰 내역이 없으면 종료
        if (auctionBidLogRepository.countByAuctionId(auctionId) == 0) {
            return null;
        }

        // 최고가 입찰자를 낙찰자로 지정
        BiddingLogEntity top1ByAuction = auctionBidLogRepository.findTop1ByAuctionIdOrderByPriceDesc(auctionId);
        ConfirmedBidsEntity confirmedBids = confirmedBidsRepository.save(
                ConfirmedBidsEntity.builder()
                        .auction(auction)
                        .biddingLog(top1ByAuction)
                        .bidder(top1ByAuction.getBidder())
                        .seller(auction.getUser())
                        .build()
        );
        // TODO : 환불 처리
        return confirmedBids;
    }

    @Transactional
    public void updateAuctionViews(Long auctionId) {
        Auction auction = auctionRepository.findById(auctionId).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "존재하지 않는 옥션입니다.", "해당 옥션을 찾을수 없습니다."));
        auction.setViewCount(auction.getViewCount() + 1);

    }

    public ConfirmedBidsEntity getConfirmedBids(Long auctionId) {
        Auction auction = auctionRepository.findById(auctionId).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "존재하지 않는 옥션입니다.", "해당 옥션을 찾을수 없습니다."));
        LocalDateTime now = LocalDateTime.now();

        if (auction.getEndTime().isAfter(now)) {
            throw new ApiException(
                    HttpStatus.BAD_REQUEST,
                    "아직 진행 중인 옥션입니다.",
                    "경매가 종료된 이후에만 이 작업을 수행할 수 있습니다."
            );
        }

        return confirmedBidsRepository.findByAuctionId(auctionId)
                .orElse(null);
    }

    // 주어진 auctionId에 대해 각 사용자별 최고 입찰가를 가져옵니다 (내림차순)
    public List<UserMaxBidProjection> getMaxBidPerUserByAuctionId(Long auctionId) {
        return auctionBidLogRepository.findMaxBidPerUserByAuctionId(auctionId);
    }
}
