package seoil.capstone.flashbid.domain.feed.controller.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import seoil.capstone.flashbid.domain.feed.dto.request.CreateFeedDto;
import seoil.capstone.flashbid.domain.feed.dto.response.FeedDto;
import seoil.capstone.flashbid.domain.feed.entity.CommentEntity;
import seoil.capstone.flashbid.domain.user.entity.Account;
import seoil.capstone.flashbid.global.common.response.ApiResult;

import java.util.List;

public interface FeedSwagger {

    @Operation(
            summary = "피드 생성",
            description = "이미지 파일과 피드 내용을 함께 전송하여 새로운 피드를 생성합니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(
                    mediaType = "multipart/form-data"

            )
    )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "피드 생성 성공",
                    content = @Content(schema = @Schema(implementation = FeedDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    ApiResult<FeedDto> createFeed(
            @Parameter(hidden = true) Account account,

            @Parameter(description = "업로드할 이미지 파일 리스트 (복수 가능)")
            @RequestPart(required = false)
            List<MultipartFile> files,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "피드 생성 데이터(JSON)",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CreateFeedDto.class))
            )
            CreateFeedDto data,

            @Parameter(hidden = true) HttpServletRequest request
    );

    @Operation(
            summary = "피드 상세 조회"
    )
    ApiResult<FeedDto> getFeedById(@PathVariable Long id, HttpServletRequest request);

//    @Operation(
//            summary = "댓글 생성"
//    )
//    ApiResult<List<CommentEntity>> createComment();
}
