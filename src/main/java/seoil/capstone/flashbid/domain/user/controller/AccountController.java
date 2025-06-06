package seoil.capstone.flashbid.domain.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import seoil.capstone.flashbid.domain.feed.dto.response.FeedDto;
import seoil.capstone.flashbid.domain.user.dto.response.UserDto;
import seoil.capstone.flashbid.domain.user.entity.Account;
import seoil.capstone.flashbid.domain.user.entity.FollowEntity;
import seoil.capstone.flashbid.domain.user.service.AccountService;
import seoil.capstone.flashbid.domain.user.service.UserService;
import seoil.capstone.flashbid.global.aop.annotation.AuthUser;
import seoil.capstone.flashbid.global.common.response.ApiResult;

import java.util.List;

@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
@Slf4j
public class AccountController {
    private final AccountService accountService;
    private final UserService userService;

    @GetMapping
    @AuthUser
    public ApiResult<Account> getUserProfile(Account user, HttpServletRequest request) {
        return ApiResult.ok(user, request);
    }

    @GetMapping("/info")
    @AuthUser
    public ApiResult<UserDto> getUserProfileInfo(Account user, HttpServletRequest request) {
        return ApiResult.ok(userService.getUserProfile(user), request);
    }

    @PatchMapping("/follow/{id}")
    @AuthUser
    public ApiResult<FollowEntity> followUser(
            Account user,
            @PathVariable(name = "id") Long followUserId,
            HttpServletRequest request
    ) {
        return ApiResult.ok(userService.followUser(user, followUserId), request);
    }

    @DeleteMapping("/unfollow/{id}")
    @AuthUser
    public ApiResult<Boolean> unFollowUser(
            Account user,
            @PathVariable(name = "id") Long unFollowUserId,
            HttpServletRequest request
    ) {
        return ApiResult.ok(userService.unFollowUser(user, unFollowUserId), request);
    }

    @GetMapping("/my/feed")
    @AuthUser
    public ApiResult<List<FeedDto>> getAllMyFeed(
            Account user,
            HttpServletRequest request
    ) {
        return ApiResult.ok(userService.getAllFeedByUserId(user.getId()), request);
    }

    @GetMapping("/{id}/feed")
    public ApiResult<List<FeedDto>> getAllUserFeed(
            @PathVariable(name = "id") Long userId,
            HttpServletRequest request
    ){
        return ApiResult.ok(userService.getAllFeedByUserId(userId), request);
    }



}
