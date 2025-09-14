package seoil.capstone.flashbid.domain.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import seoil.capstone.flashbid.domain.auction.entity.ConfirmedBidsEntity;
import seoil.capstone.flashbid.domain.feed.dto.response.FeedDto;
import seoil.capstone.flashbid.domain.file.entity.FileEntity;
import seoil.capstone.flashbid.domain.user.controller.swagger.AccountSwagger;
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

    @GetMapping
    @AuthUser
    @Override
    public ApiResult<Account> getUserProfile(Account user, HttpServletRequest request) {
        return ApiResult.ok(user, request);
    }

    @GetMapping("/status/{id}")
    public ApiResult<AccountStatusInfoProjection> getAccountStatusInfo(
            @PathVariable(name = "id") Long userId,
            HttpServletRequest request
    ) {
        return ApiResult.ok(accountRepository.findAccountStatusInfoById(userId), request);
    }

    @GetMapping("/info")
    @AuthUser
    @Override
    public ApiResult<UserDto> getUserProfileInfo(Account user, HttpServletRequest request) {
        return ApiResult.ok(userService.getUserProfile(user), request);
    }

    @PatchMapping("/follow/{id}")
    @AuthUser
    @Override
    public ApiResult<FollowEntity> followUser(
            Account user,
            @PathVariable(name = "id") Long followUserId,
            HttpServletRequest request
    ) {
        return ApiResult.ok(userService.followUser(user, followUserId), request);
    }

    @DeleteMapping("/unfollow/{id}")
    @AuthUser
    @Override
    public ApiResult<Boolean> unFollowUser(
            Account user,
            @PathVariable(name = "id") Long unFollowUserId,
            HttpServletRequest request
    ) {
        return ApiResult.ok(userService.unFollowUser(user, unFollowUserId), request);
    }

    @GetMapping("/my/feed")
    @AuthUser
    @Override
    public ApiResult<List<FeedDto>> getAllMyFeed(
            Account user,
            HttpServletRequest request
    ) {
        return ApiResult.ok(userService.getAllFeedByUserId(user.getId()), request);
    }

    @GetMapping("/{id}/feed")
    @Override
    public ApiResult<List<FeedDto>> getAllUserFeed(
            @PathVariable(name = "id") Long userId,
            HttpServletRequest request
    ){
        return ApiResult.ok(userService.getAllFeedByUserId(userId), request);
    }

    @AuthUser
    @Override
    @GetMapping("/{id}")
    public ApiResult<UserDto> getUserById(Account user,@PathVariable(name = "id") Long userId, HttpServletRequest request) {
        return ApiResult.ok(userService.getUserById(userId),request);
    }

    @AuthUser
    @Override
    @PatchMapping("/image")
    public ApiResult<FileEntity> updateProfileImage(
            Account user,
            @RequestParam("file") MultipartFile image,
            HttpServletRequest request
    ){
        return ApiResult.ok(userService.uploadProfileImage(image,user),request);
    }

    @AuthUser
    @GetMapping("/purchases")
    @Override
    public ApiResult<List<ConfirmedBidsEntity>> getPurchaseHistory(Account user, HttpServletRequest request) {
        return ApiResult.ok(userService.getPurchaseHistory(user), request);
    }

    @AuthUser
    @GetMapping("/sales")
    @Override
    public ApiResult<List<ConfirmedBidsEntity>> getSalesHistory(Account user, HttpServletRequest request) {
        return ApiResult.ok(userService.getSalesHistory(user), request);
    }

    @PatchMapping(value = "/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResult<Void> updateUserProfile(
            @PathVariable Long userId,
            @RequestPart(value = "nickname", required = false) String nickname,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage,
            HttpServletRequest request) throws IOException { // HttpServletRequest 파라미터 추가

        accountService.updateUserProfile(userId, nickname, profileImage);
        return ApiResult.ok(null, request, "프로필이 성공적으로 업데이트되었습니다.");
    }

}
