package seoil.capstone.flashbid.domain.auction.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import seoil.capstone.flashbid.domain.auction.dto.request.CreateAuctionRequestDto;
import seoil.capstone.flashbid.domain.auction.dto.request.ParticipateAuctionDto;
import seoil.capstone.flashbid.domain.auction.dto.response.AuctionDto;
import seoil.capstone.flashbid.domain.auction.dto.response.GoodsDto;
import seoil.capstone.flashbid.domain.auction.entity.Auction;
import seoil.capstone.flashbid.domain.auction.entity.AuctionParticipateEntity;
import seoil.capstone.flashbid.domain.auction.entity.BiddingLogEntity;
import seoil.capstone.flashbid.domain.auction.repository.AuctionBidLogRepository;
import seoil.capstone.flashbid.domain.auction.repository.AuctionParticipateRepository;
import seoil.capstone.flashbid.domain.auction.repository.AuctionRepository;
import seoil.capstone.flashbid.domain.file.entity.FileEntity;
import seoil.capstone.flashbid.domain.file.service.FileService;
import seoil.capstone.flashbid.domain.user.entity.Account;
import seoil.capstone.flashbid.global.common.enums.AuctionType;
import seoil.capstone.flashbid.global.common.enums.FileType;
import seoil.capstone.flashbid.global.common.error.ApiException;

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

    @Transactional
    public Auction saveAuction(Account user, CreateAuctionRequestDto dto, List<MultipartFile> images, AuctionType auctionType) {
        GoodsDto goodsDto = goodsService.uploadGoods(user, images, dto.getTitle(), dto.getDescription());
        Auction auction = Auction.builder()
                .count(0)
                .bidUnit(dto.getBidUnit())
                .endTime(dto.getEndTime())
                .auctionType(auctionType)
                .startTime(dto.getStartTime())
                .startPrice(dto.getStartPrice())
                .goods(goodsDto.getGoods())
                .user(user)
                .viewCount(0)
                .build();

        return auctionRepository.save(auction);
    }

    @Transactional
    public AuctionDto getAuctionById(Long id) {
        Auction auction = auctionRepository.findById(id).orElseThrow(() ->
                new ApiException(HttpStatus.NOT_FOUND, "", "")
        );
        List<FileEntity> allFiles = fileService.getAllFiles(auction.getGoods().getId(), FileType.GOODS);
        BiddingLogEntity bidHistory = auctionBidLogRepository.findTop1ByAuctionIdOrderByCreatedAtDesc(id);
        return new AuctionDto(
                auction,
                allFiles,
                auctionParticipateRepository.countByAuctionId(id),
                bidHistory!=null?bidHistory.getPrice():null

        );
    }
    public List<AuctionDto> getRecomendAuction(){
        List<AuctionDto> auctionDtos = new ArrayList<>();
        auctionRepository.findTop4ByOrderByCreatedAtDesc().forEach(auction->{
            BiddingLogEntity bidHistory = auctionBidLogRepository.findTop1ByAuctionIdOrderByCreatedAtDesc(auction.getId());
            auctionDtos.add(new AuctionDto(auction, fileService.getAllFiles(auction.getGoods().getId(), FileType.GOODS),
                    auctionParticipateRepository.countByAuctionId(auction.getId()),
                    bidHistory!=null?bidHistory.getPrice():null
            ));

        });
        return auctionDtos;
    }

    @Transactional
    public List<AuctionDto> queryAllAuction() {
        List<AuctionDto> auctionDtos = new ArrayList<>();
        auctionRepository.findAllByOrderByCreatedAtDesc().forEach(auction -> {

            BiddingLogEntity bidHistory = auctionBidLogRepository.findTop1ByAuctionIdOrderByCreatedAtDesc(auction.getId());
            auctionDtos.add(new AuctionDto(auction, fileService.getAllFiles(auction.getGoods().getId(), FileType.GOODS),
                    auctionParticipateRepository.countByAuctionId(auction.getId()),
                    bidHistory!=null?bidHistory.getPrice():null
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
}
