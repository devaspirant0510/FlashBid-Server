package seoil.capstone.flashbid.domain.auction.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import seoil.capstone.flashbid.domain.auction.dto.request.CreateAuctionRequestDto;
import seoil.capstone.flashbid.domain.auction.dto.response.GoodsDto;
import seoil.capstone.flashbid.domain.auction.entity.Auction;
import seoil.capstone.flashbid.domain.auction.repository.AuctionRepository;
import seoil.capstone.flashbid.domain.file.service.FileService;
import seoil.capstone.flashbid.domain.user.entity.Account;
import seoil.capstone.flashbid.global.common.enums.AuctionType;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuctionService {
    private final FileService fileService;
    private final GoodsService goodsService;
    private final AuctionRepository auctionRepository;

    @Transactional
    public Auction saveLiveAuction(Account user, CreateAuctionRequestDto dto, List<MultipartFile> images) {
        GoodsDto goodsDto = goodsService.uploadGoods(user, images, dto.getTitle(), dto.getDescription());
        Auction auction = Auction.builder()
                .count(0)
                .bidUnit(dto.getBidUnit())
                .endTime(dto.getEndTime())
                .auctionType(AuctionType.LIVE)
                .startTime(dto.getStartTime())
                .startPrice(dto.getStartPrice())
                .goods(goodsDto.getGoods())
                .user(user)
                .viewCount(0)
                .build();

        return auctionRepository.save(auction);


    }
}
