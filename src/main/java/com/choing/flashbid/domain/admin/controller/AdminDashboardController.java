package com.choing.flashbid.domain.admin.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.choing.flashbid.domain.admin.dto.response.DashboardOverViewDto;
import com.choing.flashbid.domain.admin.service.AdminService;
import com.choing.flashbid.global.common.response.ApiResult;


@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/admin/dashboard")
public class AdminDashboardController {
    private final AdminService adminService;

    @GetMapping("/overview")
    public ApiResult<DashboardOverViewDto> getOverview() {
        return ApiResult.ok(adminService.getDashboardOverViewDto());
    }

}
