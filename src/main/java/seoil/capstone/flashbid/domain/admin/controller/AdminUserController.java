package seoil.capstone.flashbid.domain.admin.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import seoil.capstone.flashbid.domain.admin.controller.swagger.AdminSwagger;
import seoil.capstone.flashbid.domain.admin.dto.request.AdminRegisterDto;
import seoil.capstone.flashbid.domain.user.dto.response.AccountDetailDto;
import seoil.capstone.flashbid.domain.admin.projection.BidInfoProjection;
import seoil.capstone.flashbid.domain.admin.projection.CategoryAuctionChartProjection;
import seoil.capstone.flashbid.domain.admin.service.AdminService;
import seoil.capstone.flashbid.global.common.enums.UserStatus;
import seoil.capstone.flashbid.global.common.response.ApiResult;

import java.util.List;


@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/admin/user")
public class AdminUserController implements AdminSwagger {
    private final AdminService adminService;

    @PostMapping("/register")
    public ApiResult<String> postRegisterAdmin(@RequestBody AdminRegisterDto registerDto){
        adminService.registerAdmin(registerDto);
        return null;
    }

    @GetMapping()
    public ApiResult<Page<AccountDetailDto>> getUserList(
            @RequestParam(required = false) UserStatus status,
            @RequestParam(required = false) String name,
            @RequestParam() Integer page,
            @RequestParam() Integer size
    ) {
        return ApiResult.ok(adminService.getAuctionUsers(page, size, status));
    }


    @GetMapping("/chart/user/top")
    public ApiResult<?> getTopUsers() {
        return null;
    }

    @GetMapping("/chart/auction/category/count")
    public ApiResult<List<CategoryAuctionChartProjection>> getCategoryCountForAuction(
            HttpServletRequest request
    ) {
        return ApiResult.ok(adminService.getChartForCategoryCount());
    }

    @GetMapping("/chart/bidlog-info")
    public ApiResult<List<BidInfoProjection>> getBiddingLogInfoList(
            HttpServletRequest request
    ) {
        return ApiResult.ok(adminService.getBiddingLogInfoList());
    }
}
