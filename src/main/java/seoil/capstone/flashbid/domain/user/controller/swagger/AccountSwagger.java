package seoil.capstone.flashbid.domain.user.controller.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
import seoil.capstone.flashbid.domain.feed.dto.response.FeedDto;
import seoil.capstone.flashbid.domain.user.dto.response.UserDto;
import seoil.capstone.flashbid.domain.user.entity.Account;
import seoil.capstone.flashbid.domain.user.entity.FollowEntity;
import seoil.capstone.flashbid.global.common.response.ApiResult;

import java.util.List;

@Tag(name = "Account", description = "유저 프로필 관련 API")
public interface AccountSwagger {

    @Operation(summary = "내 프로필 조회", description = "현재 로그인된 사용자의 정보를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(schema = @Schema(implementation = Account.class)))
    @GetMapping
    ApiResult<Account> getUserProfile(
            @Parameter(hidden = true) Account user,
            HttpServletRequest request
    );

    @Operation(summary = "내 프로필 정보 조회", description = "사용자 추가 정보(닉네임, 소개 등)를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(schema = @Schema(implementation = UserDto.class)))
    @GetMapping("/info")
    ApiResult<UserDto> getUserProfileInfo(
            @Parameter(hidden = true) Account user,
            HttpServletRequest request
    );

    @Operation(summary = "다른 유저 팔로우", description = "특정 유저를 팔로우합니다.")
    @ApiResponse(responseCode = "200", description = "팔로우 성공",
            content = @Content(schema = @Schema(implementation = FollowEntity.class)))
    @PatchMapping("/follow/{id}")
    ApiResult<FollowEntity> followUser(
            @Parameter(hidden = true) Account user,
            @Parameter(description = "팔로우할 유저 ID") @PathVariable("id") Long followUserId,
            HttpServletRequest request
    );

    @Operation(summary = "다른 유저 언팔로우", description = "특정 유저를 언팔로우합니다.")
    @ApiResponse(responseCode = "200", description = "언팔로우 성공",
            content = @Content(schema = @Schema(implementation = Boolean.class)))
    @DeleteMapping("/unfollow/{id}")
    ApiResult<Boolean> unFollowUser(
            @Parameter(hidden = true) Account user,
            @Parameter(description = "언팔할 유저 ID") @PathVariable("id") Long unFollowUserId,
            HttpServletRequest request
    );

    @Operation(summary = "내 피드 전체 조회", description = "내가 올린 피드를 모두 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(schema = @Schema(implementation = FeedDto.class)))
    @GetMapping("/my/feed")
    ApiResult<List<FeedDto>> getAllMyFeed(
            @Parameter(hidden = true) Account user,
            HttpServletRequest request
    );

    @Operation(summary = "특정 유저 피드 조회", description = "해당 유저가 올린 피드를 모두 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(schema = @Schema(implementation = FeedDto.class)))
    @GetMapping("/{id}/feed")
    ApiResult<List<FeedDto>> getAllUserFeed(
            @Parameter(description = "유저 ID") @PathVariable("id") Long userId,
            HttpServletRequest request
    );
}
