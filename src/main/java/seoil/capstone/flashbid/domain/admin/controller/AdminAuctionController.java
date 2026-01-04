package seoil.capstone.flashbid.domain.admin.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import seoil.capstone.flashbid.domain.admin.controller.swagger.AdminSwagger;
import seoil.capstone.flashbid.domain.admin.projection.BidInfoProjection;
import seoil.capstone.flashbid.domain.admin.projection.CategoryAuctionChartProjection;
import seoil.capstone.flashbid.domain.admin.service.AdminService;
import seoil.capstone.flashbid.global.common.enums.AuctionStatus;
import seoil.capstone.flashbid.global.common.enums.AuctionType;
import seoil.capstone.flashbid.global.common.response.ApiResult;

import java.util.List;


@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/admin/auction")
public class AdminAuctionController implements AdminSwagger {
    private final AdminService adminService;

    @GetMapping("live")
    public ApiResult<Page<?>> getLiveAuctionList(
            @RequestParam(required = false) AuctionStatus status,
            @RequestParam(required = false) String category,
            @RequestParam Integer page,
            @RequestParam Integer size
    ){
        return ApiResult.ok(
                adminService.getAdminAuctionList(AuctionType.LIVE,category,status,page,size),
                "실시간 경매 조회 완료"
        );
    }

    @GetMapping("blind")
    public ApiResult<Page<?>> getBlindAuctionList(
            @RequestParam Integer page,
            @RequestParam Integer size
    ){
        return ApiResult.ok(
                adminService.getAdminAuctionList(AuctionType.BLIND,null,null,page,size),
                "실시간 경매 조회 완료"
        );
    }

    @GetMapping("/chart/category/count")
    public ApiResult<List<CategoryAuctionChartProjection>> getCategoryCountForAuction(
    ){
        return ApiResult.ok(adminService.getChartForCategoryCount());
    }
    @GetMapping("/chart/bidlog-info")
    public ApiResult<List<BidInfoProjection>> getBiddingLogInfoList(
            HttpServletRequest request
    ) {
        return ApiResult.ok(adminService.getBiddingLogInfoList());
    }
}
