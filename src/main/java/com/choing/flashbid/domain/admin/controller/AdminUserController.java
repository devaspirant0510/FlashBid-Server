package com.choing.flashbid.domain.admin.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import com.choing.flashbid.domain.admin.controller.swagger.AdminSwagger;
import com.choing.flashbid.domain.admin.dto.request.AdminRegisterDto;
import com.choing.flashbid.domain.user.dto.response.AccountDetailDto;
import com.choing.flashbid.domain.admin.projection.BidInfoProjection;
import com.choing.flashbid.domain.admin.projection.CategoryAuctionChartProjection;
import com.choing.flashbid.domain.admin.service.AdminService;
import com.choing.flashbid.global.common.enums.UserStatus;
import com.choing.flashbid.global.common.response.ApiResult;

import java.util.List;


@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/admin/user")
public class AdminUserController implements AdminSwagger {
    private final AdminService adminService;
    @GetMapping("ping")
    public String ping() {
        return "pong";
    }

    @PostMapping("/register")
    public ApiResult<String> postRegisterAdmin(@RequestBody AdminRegisterDto registerDto){
        adminService.registerAdmin(registerDto);
        return ApiResult.ok("회원가입 성공");
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
