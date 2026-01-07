package seoil.capstone.flashbid.domain.admin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import seoil.capstone.flashbid.domain.admin.dto.request.AdminRegisterDto;
import seoil.capstone.flashbid.domain.admin.dto.response.AdminAuctionDto;
import seoil.capstone.flashbid.domain.admin.dto.response.DashboardOverViewDto;
import seoil.capstone.flashbid.domain.admin.projection.*;
import seoil.capstone.flashbid.domain.admin.repository.jpa.AccountAdminRepository;
import seoil.capstone.flashbid.domain.admin.repository.jpa.AuctionAdminRepository;
import seoil.capstone.flashbid.domain.admin.repository.jpa.BidLogAdminRepository;
import seoil.capstone.flashbid.domain.auction.repository.AuctionBidLogRepository;
import seoil.capstone.flashbid.domain.auction.service.AuctionService;
import seoil.capstone.flashbid.domain.category.service.CategoryService;
import seoil.capstone.flashbid.domain.user.dto.response.AccountDetailDto;
import seoil.capstone.flashbid.domain.user.entity.Account;
import seoil.capstone.flashbid.domain.user.repository.AccountRepository;
import seoil.capstone.flashbid.global.common.enums.*;
import seoil.capstone.flashbid.global.common.error.ApiException;
import seoil.capstone.flashbid.global.core.provider.HashProvider;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService {
    private final AuctionAdminRepository auctionAdminRepository;
    private final AccountAdminRepository accountAdminRepository;
    private final BidLogAdminRepository bidLogAdminRepository;
    private final CategoryService categoryService;
    private final AuctionService auctionService;
    private final AuctionBidLogRepository auctionBidLogRepository;
    private final HashProvider hashProvider;
    private final AccountRepository accountRepository;

    public Page<AccountDetailDto> getAuctionUsers(int page, int size, UserStatus userStatus) {
        log.info("status :{}", userStatus);
        return accountAdminRepository.findAccounts(userStatus, PageRequest.of(page, size));
    }

    public Page<AdminAuctionDto> getAdminAuctionList(
            AuctionType auctionType,
            String categoryName,
            AuctionStatus auctionStatus,
            int currentPage,
            int pageSize) {
        int pageGroupSize = 10;
        if (currentPage <= 0) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "잘못된 파라미터", "page 는 1 이상의 값이여야 합니다.");
        }
        Long categoryId = categoryService.getCategoryId(categoryName);

        int page = ((currentPage - 1) / pageSize)*pageSize * pageGroupSize;
        int pageOffset = page + pageSize * pageGroupSize;
        List<AuctionAdminProjection> allAdminAuctions = auctionAdminRepository.findAllAdminAuctions(
                categoryId,
                auctionStatus==null?null:auctionStatus.ordinal(),
                auctionType.ordinal(),
                pageSize,
                pageOffset
        );
        System.out.println("allAdminAuctions: " + allAdminAuctions.get(0).getAuctionType());
        List<AdminAuctionDto> convertAdminAuctions = allAdminAuctions.stream().map(AdminAuctionDto::from).toList();
        int countAdminAuctions = auctionAdminRepository.countByAdminAuction(
                categoryId,
                auctionStatus,
                auctionType
        );
        return new PageImpl<>(convertAdminAuctions, PageRequest.of(currentPage, pageSize), countAdminAuctions);

    }

    public List<CategoryAuctionChartProjection> getChartForCategoryCount() {
        return auctionAdminRepository.findCategoryAuctionCount();
    }

    public List<BidInfoProjection> getBiddingLogInfoList() {
        return bidLogAdminRepository.getBiddingLogInfoList();
    }

    @Transactional
    public DashboardOverViewDto getDashboardOverViewDto() {
        DashboardOverViewDto dashboardOverViewDto = new DashboardOverViewDto();

        AuctionDashboardProjection blindDashboardStats = auctionAdminRepository.getDashboardStats(AuctionType.BLIND.ordinal());
        AuctionDashboardProjection liveDashboardStats = auctionAdminRepository.getDashboardStats(AuctionType.LIVE.ordinal());
        dashboardOverViewDto.setLiveAuctionStats(liveDashboardStats);
        dashboardOverViewDto.setBlindAuctionStats(blindDashboardStats);

        LocalDate today = LocalDate.now();

        LocalDateTime todayStart = today.atStartOfDay(); // 오늘 00:00
        LocalDateTime tomorrowStart = today.plusDays(1).atStartOfDay(); // 내일 00:00
        LocalDateTime yesterdayStart = today.minusDays(1).atStartOfDay(); // 어제 00:00

        BiddingDashboardProjection liveBidDashboardStats = bidLogAdminRepository.getDashboardStats(
                todayStart,
                tomorrowStart,
                yesterdayStart,
                AuctionType.LIVE.ordinal()
        );
        BiddingDashboardProjection blindBidDashboardStats = bidLogAdminRepository.getDashboardStats(
                todayStart,
                tomorrowStart,
                yesterdayStart,
                AuctionType.BLIND.ordinal()
        );
        dashboardOverViewDto.setLiveBiddingDashboardStats(liveBidDashboardStats);
        dashboardOverViewDto.setBlindBiddingDashboardStats(blindBidDashboardStats);

        AccountDashboardProjection accountDashboardStats = accountAdminRepository.getAccountDashboardStats(
                todayStart,
                tomorrowStart,
                yesterdayStart
        );

        dashboardOverViewDto.setAccountDashboardStats(accountDashboardStats);


        return dashboardOverViewDto;
    }

    @Transactional
    public void registerAdmin(AdminRegisterDto registerDto) {
        BCryptPasswordEncoder encode = new BCryptPasswordEncoder();
        if(accountRepository.existsByEmailAndUserType(registerDto.getEmail(), UserType.ADMIN)){
            throw new ApiException(HttpStatus.CONFLICT,"회원가입 실패","이미 가입된 계정입니다");
        }
        accountRepository.save(
                Account.builder()
                        .email(registerDto.getEmail())
                        .isVerified(false)
                        .password(encode.encode(registerDto.getPassword()))
                        .loginType(LoginType.EMAIL)
                        .nickname(registerDto.getUsername())
                        .userStatus(UserStatus.ACTIVE) // 테스트용으로 관리자 가입시 바로 active
                        .userType(UserType.ADMIN)
                        .uuid(UUID.randomUUID().toString())
                        .build()
        );
    }
}
