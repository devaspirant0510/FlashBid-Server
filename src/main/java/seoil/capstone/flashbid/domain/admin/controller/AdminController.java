package seoil.capstone.flashbid.domain.admin.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import seoil.capstone.flashbid.domain.admin.controller.swagger.AdminSwagger;
import seoil.capstone.flashbid.domain.admin.projection.BidInfoProjection;
import seoil.capstone.flashbid.domain.admin.projection.CategoryAuctionChartProjection;
import seoil.capstone.flashbid.domain.admin.service.AdminService;
import seoil.capstone.flashbid.global.common.response.ApiResult;

import java.util.List;


@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/admin")
public class AdminController implements AdminSwagger {
    private final AdminService adminService;


    @GetMapping("/chart/user/top")
    public ApiResult<?> getTopUsers() {
        return null;
    }

    @GetMapping("/chart/auction/category/count")
    public ApiResult<List<CategoryAuctionChartProjection>> getCategoryCountForAuction(
            HttpServletRequest request
    ){
        return ApiResult.ok(adminService.getChartForCategoryCount(),request);
    }
    @GetMapping("/chart/bidlog-info")
    public ApiResult<List<BidInfoProjection>> getBiddingLogInfoList(
            HttpServletRequest request
    ) {
        return ApiResult.ok(adminService.getBiddingLogInfoList(), request);
    }
}
