package com.choing.flashbid.domain.feed.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.choing.flashbid.domain.feed.dto.response.FeedDto;
import com.choing.flashbid.domain.feed.service.FeedService;
import com.choing.flashbid.domain.user.entity.Account;
import com.choing.flashbid.global.aop.annotation.AuthUser;
import com.choing.flashbid.global.common.response.ApiResult;

import java.util.List;


@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v2/feed")
public class FeedV2Controller {
    private final FeedService feedService;

    @GetMapping("page")
    @AuthUser
    public ApiResult<Slice<FeedDto>> findFeedQueryPaging(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "8") Integer size,
            Account account
    ) {
        return ApiResult.ok(feedService.getFeedQuery(page - 1, size, account), "피드 조회 성공 ");
    }

    @GetMapping()
    @AuthUser
    public ApiResult<List<FeedDto>> findFeedQuery(
            @RequestParam(value = "cursor", required = false)
            Long cursor,
            Account account
    ) {
        return ApiResult.ok(feedService.getFeedQueryCursor(cursor,account));

    }
}
