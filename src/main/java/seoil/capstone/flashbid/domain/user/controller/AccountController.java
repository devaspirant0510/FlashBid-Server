package seoil.capstone.flashbid.domain.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import seoil.capstone.flashbid.domain.auction.dto.response.AuctionDto;
import seoil.capstone.flashbid.domain.auction.entity.ConfirmedBidsEntity;
import seoil.capstone.flashbid.domain.feed.dto.response.FeedDto;
import seoil.capstone.flashbid.domain.file.entity.FileEntity;
import seoil.capstone.flashbid.domain.payment.entity.PointHistoryEntity;
import seoil.capstone.flashbid.domain.payment.service.PaymentService;
import seoil.capstone.flashbid.domain.user.controller.swagger.AccountSwagger;
import seoil.capstone.flashbid.domain.user.dto.response.AccountDto;
import seoil.capstone.flashbid.domain.user.dto.response.FollowUserDto;
import seoil.capstone.flashbid.domain.user.dto.response.UserDto;
import seoil.capstone.flashbid.domain.user.entity.Account;
import seoil.capstone.flashbid.domain.user.entity.FollowEntity;
import seoil.capstone.flashbid.domain.user.projection.AccountStatusInfoProjection;
import seoil.capstone.flashbid.domain.user.repository.AccountRepository;
import seoil.capstone.flashbid.domain.user.service.AccountService;
import seoil.capstone.flashbid.domain.user.service.UserService;
import seoil.capstone.flashbid.global.aop.annotation.AuthUser;
import seoil.capstone.flashbid.global.common.response.ApiResult;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
@Slf4j
public class    AccountController implements AccountSwagger {
    private final AccountService accountService;
    private final UserService userService;
    private final AccountRepository accountRepository;
    private final PaymentService paymentService;

    @GetMapping
    @AuthUser
    @Override
    public ApiResult<Account> getUserProfile(Account user, HttpServletRequest request) {
        return ApiResult.ok(user);
    }

    @GetMapping("/status/{id}")
    public ApiResult<AccountStatusInfoProjection> getAccountStatusInfo(
            @PathVariable(name = "id") Long userId,
            HttpServletRequest request
    ) {
        return ApiResult.ok(accountRepository.findAccountStatusInfoById(userId));
    }

    @GetMapping("/info")
    @AuthUser
    @Override
    public ApiResult<UserDto> getUserProfileInfo(Account user, HttpServletRequest request) {
        return ApiResult.ok(userService.getUserProfile(user));
    }

    @PatchMapping("/follow/{id}")
    @AuthUser
    @Override
    public ApiResult<FollowEntity> followUser(
            Account user,
            @PathVariable(name = "id") Long followUserId,
            HttpServletRequest request
    ) {
        return ApiResult.ok(userService.followUser(user, followUserId));
    }

    @DeleteMapping("/unfollow/{id}")
    @AuthUser
    @Override
    public ApiResult<Boolean> unFollowUser(
            Account user,
            @PathVariable(name = "id") Long unFollowUserId,
            HttpServletRequest request
    ) {
        return ApiResult.ok(userService.unFollowUser(user, unFollowUserId));
    }

    @GetMapping("/my/feed")
    @AuthUser
    @Override
    public ApiResult<List<FeedDto>> getAllMyFeed(
            Account user,
            HttpServletRequest request
    ) {
        return ApiResult.ok(userService.getAllFeedByUserId(user.getId()));
    }

    @GetMapping("/{id}/feed")
    @Override
    public ApiResult<List<FeedDto>> getAllUserFeed(
            @PathVariable(name = "id") Long userId,
            HttpServletRequest request
    ){
        return ApiResult.ok(userService.getAllFeedByUserId(userId));
    }

    @AuthUser
    @Override
    @GetMapping("/{id}")
    public ApiResult<UserDto> getUserById(Account user,@PathVariable(name = "id") Long userId, HttpServletRequest request) {
        return ApiResult.ok(userService.getUserById(user, userId));
    }

    @AuthUser
    @Override
    @PatchMapping("/image")
    public ApiResult<FileEntity> updateProfileImage(
            Account user,
            @RequestParam("file") MultipartFile image,
            HttpServletRequest request
    ){
        return ApiResult.ok(userService.uploadProfileImage(image,user));
    }

    @AuthUser
    @GetMapping("/purchases")
    @Override
    public ApiResult<List<AuctionDto>> getPurchaseHistory(Account user, HttpServletRequest request) { // [변경] List<ConfirmedBidsEntity> -> List<AuctionDto>
        return ApiResult.ok(userService.getPurchaseHistory(user));
    }

    @AuthUser
    @GetMapping("/sales")
    @Override
    public ApiResult<List<AuctionDto>> getSalesHistory(Account user, HttpServletRequest request) { // [변경] List<ConfirmedBidsEntity> -> List<AuctionDto>
        return ApiResult.ok(userService.getSalesHistory(user));
    }

    @PatchMapping(value = "/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResult<Void> updateUserProfile(
            @PathVariable Long userId,
            @RequestPart(value = "nickname", required = false) String nickname,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage,
            HttpServletRequest request) throws IOException { // HttpServletRequest 파라미터 추가

        accountService.updateUserProfile(userId, nickname, profileImage);
        return ApiResult.ok(null, "프로필이 성공적으로 업데이트되었습니다.");
    }

    @AuthUser
    @GetMapping("/{userId}/followers")
    public ApiResult<List<FollowUserDto>> getFollowerList(
            Account user,
            @PathVariable Long userId,
            HttpServletRequest request
    ) {
        return ApiResult.ok(userService.getFollowerList(user, userId));
    }

    @AuthUser
    @GetMapping("/{userId}/followings")
    public ApiResult<List<FollowUserDto>> getFollowingList(
            Account user,
            @PathVariable Long userId,
            HttpServletRequest request
    ) {
        return ApiResult.ok(userService.getFollowingList(user, userId));
    }

    @AuthUser
    @GetMapping("/my/point-history")
    public ApiResult<Slice<PointHistoryEntity>> getMyPointHistory(
            Account user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request
    ) {
        return ApiResult.ok(paymentService.getPointHistoryList(user, page, size));
    }

    @AuthUser
    @GetMapping("/my")
    public ApiResult<AccountDto> getMyAccountInfo(
            Account user
    ) {
        return ApiResult.ok(new AccountDto(
                user.getId(),
                user.getEmail(),
                user.getNickname(),
                user.getProfileUrl(),
                user.getPoint()
        ),"내 정보 조회 성공");
    }

}
