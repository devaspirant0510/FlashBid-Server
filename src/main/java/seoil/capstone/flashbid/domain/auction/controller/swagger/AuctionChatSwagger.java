package seoil.capstone.flashbid.domain.auction.controller.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PathVariable;
import seoil.capstone.flashbid.domain.auction.entity.AuctionChatEntity;
import seoil.capstone.flashbid.global.common.response.ApiResult;

import java.util.List;

@Tag(name = "Auction Chat API", description = "경매 채팅 관련 API")
public interface AuctionChatSwagger {
    @Operation(summary = "경매 채팅 내역 조회", description = "경매 ID로 해당 경매의 채팅 내역을 조회합니다.")
    ApiResult<List<AuctionChatEntity>> getAllChatList(
            @Parameter(description = "경매 ID") @PathVariable(name = "id") Long auctionId,
            HttpServletRequest request
    );
}
