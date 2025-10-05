package seoil.capstone.flashbid.domain.auction.controller.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import seoil.capstone.flashbid.domain.auction.dto.response.GoodsDto;
import seoil.capstone.flashbid.domain.user.entity.Account;
import seoil.capstone.flashbid.global.common.response.ApiResult;

import java.util.List;

@Tag(name = "Goods API", description = "상품 관련 API")
public interface GoodsSwagger {
    @Operation(summary = "상품 생성", description = "상품을 생성합니다.")
    ApiResult<GoodsDto> createGoods(
            @Parameter(hidden = true) Account user,
            @Parameter(description = "이미지 파일") @RequestParam("files") List<MultipartFile> files,
            @Parameter(description = "상품 제목") @RequestParam("title") String title,
            @Parameter(description = "상품 설명") @RequestParam("description") String description
    );
}