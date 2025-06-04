package seoil.capstone.flashbid.domain.feed.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import seoil.capstone.flashbid.domain.feed.dto.request.CreateFeedDto;
import seoil.capstone.flashbid.domain.feed.dto.response.FeedDto;
import seoil.capstone.flashbid.domain.feed.service.FeedService;
import seoil.capstone.flashbid.domain.user.entity.Account;
import seoil.capstone.flashbid.global.aop.annotation.AuthUser;
import seoil.capstone.flashbid.global.common.response.ApiResult;

import java.util.List;


@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/feed")
public class FeedController {
    private final FeedService feedService;

    @AuthUser
    @PostMapping
    public ApiResult<FeedDto> createFeed(
            Account account,
            @RequestParam("files") List<MultipartFile> files,
            @RequestPart CreateFeedDto data,
            HttpServletRequest request
    ) {
        FeedDto feed = feedService.createFeed(account, files, data);
        return ApiResult.ok(feed,request);
    }

}
