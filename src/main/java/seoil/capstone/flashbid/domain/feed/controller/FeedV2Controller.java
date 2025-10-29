package seoil.capstone.flashbid.domain.feed.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import seoil.capstone.flashbid.domain.feed.projection.FeedProjection;
import seoil.capstone.flashbid.domain.feed.service.FeedService;
import seoil.capstone.flashbid.global.common.response.ApiResult;


@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v2/feed")
public class FeedV2Controller {
    private final FeedService feedService;

    @GetMapping
    public ApiResult<Slice<FeedProjection>> findFeedQuery(
            @RequestParam("page") Integer page,
            @RequestParam(value = "size" ,defaultValue = "8") Integer size
    ) {
        return ApiResult.ok(feedService.getFeedQuery(page, size), "피드 조회 성공 ");


    }
}
