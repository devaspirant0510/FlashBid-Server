package seoil.capstone.flashbid.domain.auction.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import seoil.capstone.flashbid.domain.auction.dto.request.CreateAuctionRequestDto;
import seoil.capstone.flashbid.domain.auction.entity.Auction;
import seoil.capstone.flashbid.domain.auction.repository.AuctionRepository;
import seoil.capstone.flashbid.domain.auction.service.AuctionService;
import seoil.capstone.flashbid.domain.user.entity.Account;
import seoil.capstone.flashbid.global.aop.annotation.AuthUser;
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
            Account account,
            @RequestParam("files") List<MultipartFile> files,
            @RequestPart("data") CreateAuctionRequestDto dto,
            HttpServletRequest request
    ) {
        Auction auction = auctionService.saveLiveAuction(account, dto, files);
        return ApiResult.created(auction, request);

    }

    @GetMapping("/test/all")
    public ApiResult<List<Auction>> getAllTestAuction(HttpServletRequest request){
        List<Auction> all = auctionRepository.findAll();
        return ApiResult.ok(all,request);
    }


}
