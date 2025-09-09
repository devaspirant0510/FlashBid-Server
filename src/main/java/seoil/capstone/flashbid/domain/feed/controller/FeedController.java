package seoil.capstone.flashbid.domain.feed.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import seoil.capstone.flashbid.domain.feed.controller.swagger.FeedSwagger;
import seoil.capstone.flashbid.domain.feed.dto.request.CreateCommentDto;
import seoil.capstone.flashbid.domain.feed.dto.request.CreateFeedDto;
import seoil.capstone.flashbid.domain.feed.dto.response.FeedDto;
import seoil.capstone.flashbid.domain.feed.entity.CommentEntity;
import seoil.capstone.flashbid.domain.feed.entity.LikeEntity;
import seoil.capstone.flashbid.domain.feed.service.FeedService;
import seoil.capstone.flashbid.domain.user.entity.Account;
import seoil.capstone.flashbid.global.aop.annotation.AuthUser;
import seoil.capstone.flashbid.global.common.response.ApiResult;

import java.util.List;


@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/feed")
public class FeedController implements FeedSwagger {
    private final FeedService feedService;

    @AuthUser
    @PostMapping
    @Override
    public ApiResult<FeedDto> createFeed(
            Account account,
            @RequestParam(value = "files",required = false) List<MultipartFile> files,
            @RequestPart CreateFeedDto data,
            HttpServletRequest request
    ) {
        FeedDto feed = feedService.createFeed(account, files, data);
        return ApiResult.created(feed, request);
    }

    @GetMapping("/hot")
    public ApiResult<List<FeedDto>> getHotFeed(HttpServletRequest request){
        return ApiResult.ok(feedService.getHotFeed(),request,"성공");
    }
    @GetMapping("/{id}")
    @Override
    public ApiResult<FeedDto> getFeedById(@PathVariable Long id, HttpServletRequest request) {
        return ApiResult.ok(feedService.getFeedById(id), request);
    }



    @GetMapping("/test-all")
    @AuthUser
    @Override
    public ApiResult<List<FeedDto>> getTestFeedAll(Account account,HttpServletRequest request) {
        log.info("리스트 조회x");
        return ApiResult.ok(feedService.getTestAllFeed(account), request);
    }

    @PatchMapping("/{id}/like")
    @AuthUser
    @Override
    public ApiResult<LikeEntity> likePost(
            Account user,
            @PathVariable(name = "id") Long postId,
            HttpServletRequest request
    ) {
        return ApiResult.ok(feedService.likePost(user, postId), request);
    }

    @DeleteMapping("/{id}/unlike")
    @AuthUser
    @Override
    public ApiResult<Boolean> unLikePost(
            Account user,
            @PathVariable(name = "id") Long postId,
            HttpServletRequest request
    ) {
        return ApiResult.ok(feedService.unLikePost(user, postId), request);
    }

    @PostMapping("/comment")
    @AuthUser
    @Override
    public ApiResult<CommentEntity> createComment(
            Account user,
            HttpServletRequest request,
            @RequestBody CreateCommentDto dto
    ) {
        return ApiResult.created(feedService.createComent(user, dto.getFeedId(), dto), request);
    }

    @GetMapping("/comment/{id}/root")
    @Override
    public ApiResult<List<CommentEntity>> getAllRootComment(
            @PathVariable(name = "id") Long feedId,
            HttpServletRequest request
    ){
        return ApiResult.ok(feedService.getAllRootComment(feedId),request);
    }

    @GetMapping("/comment/reply/{id}")
    @Override
    public ApiResult<List<CommentEntity>> getAllCommentByReply(
            @PathVariable(name = "id") Long replyId,
            HttpServletRequest request
    ){
        return ApiResult.ok(feedService.getAllCommentByReplyId(replyId),request);
    }

}
