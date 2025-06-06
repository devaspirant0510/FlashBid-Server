package seoil.capstone.flashbid.domain.feed.controller.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import seoil.capstone.flashbid.domain.feed.dto.request.CreateCommentDto;
import seoil.capstone.flashbid.domain.feed.dto.request.CreateFeedDto;
import seoil.capstone.flashbid.domain.feed.dto.response.FeedDto;
import seoil.capstone.flashbid.domain.feed.entity.CommentEntity;
import seoil.capstone.flashbid.domain.feed.entity.LikeEntity;
import seoil.capstone.flashbid.domain.user.entity.Account;
import seoil.capstone.flashbid.global.common.response.ApiResult;

import java.util.List;

public interface FeedSwagger {

    @Operation(summary = "피드 생성", description = "이미지 파일과 피드 데이터를 함께 보내 새로운 피드를 생성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "피드 생성 성공", content = @Content(schema = @Schema(implementation = FeedDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    ApiResult<FeedDto> createFeed(
            @Parameter(hidden = true) Account account,
            @RequestPart(value = "files", required = false) List<MultipartFile> files,
            @RequestPart(value = "data") CreateFeedDto data,
            @Parameter(hidden = true) HttpServletRequest request
    );

    @Operation(summary = "피드 상세 조회", description = "피드 ID를 통해 피드의 상세 내용을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "피드 조회 성공"),
            @ApiResponse(responseCode = "404", description = "피드가 존재하지 않음")
    })
    ApiResult<FeedDto> getFeedById(@PathVariable Long id, HttpServletRequest request);

    @Operation(summary = "피드 전체 테스트 조회", description = "모든 피드를 테스트용으로 조회합니다.")
    ApiResult<List<FeedDto>> getTestFeedAll(HttpServletRequest request);

    @Operation(summary = "피드 좋아요", description = "사용자가 해당 피드에 좋아요를 누릅니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "좋아요 성공"),
            @ApiResponse(responseCode = "404", description = "피드가 존재하지 않음")
    })
    ApiResult<LikeEntity> likePost(
            @Parameter(hidden = true) Account user,
            @PathVariable(name = "id") Long postId,
            @Parameter(hidden = true) HttpServletRequest request
    );

    @Operation(summary = "피드 좋아요 취소", description = "사용자가 해당 피드의 좋아요를 취소합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "좋아요 취소 성공"),
            @ApiResponse(responseCode = "404", description = "피드 또는 좋아요가 존재하지 않음")
    })
    ApiResult<Boolean> unLikePost(
            @Parameter(hidden = true) Account user,
            @PathVariable(name = "id") Long postId,
            @Parameter(hidden = true) HttpServletRequest request
    );

    @Operation(summary = "댓글 작성", description = "피드에 새로운 댓글을 작성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "댓글 작성 성공", content = @Content(schema = @Schema(implementation = CommentEntity.class))),
            @ApiResponse(responseCode = "404", description = "피드가 존재하지 않음")
    })
    ApiResult<CommentEntity> createComment(
            @Parameter(hidden = true) Account user,
            @Parameter(hidden = true) HttpServletRequest request,
            @RequestBody CreateCommentDto dto
    );

    @Operation(summary = "피드의 모든 루트 댓글 조회", description = "피드 ID를 통해 해당 피드의 최상위 댓글들을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "댓글 조회 성공")
    })
    ApiResult<List<CommentEntity>> getAllRootComment(
            @PathVariable(name = "id") Long feedId,
            @Parameter(hidden = true) HttpServletRequest request
    );

    @Operation(summary = "댓글의 모든 대댓글 조회", description = "대댓글의 부모 댓글 ID를 통해 해당 댓글의 모든 대댓글을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "대댓글 조회 성공")
    })
    ApiResult<List<CommentEntity>> getAllCommentByReply(
            @PathVariable(name = "id") Long replyId,
            @Parameter(hidden = true) HttpServletRequest request
    );
}
