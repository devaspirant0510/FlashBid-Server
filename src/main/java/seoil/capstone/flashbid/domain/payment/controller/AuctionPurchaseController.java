package seoil.capstone.flashbid.domain.payment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import seoil.capstone.flashbid.domain.payment.dto.PurchaseConfirmRequest;
import seoil.capstone.flashbid.domain.payment.service.AuctionPurchaseService;
import seoil.capstone.flashbid.domain.user.entity.Account;
import seoil.capstone.flashbid.global.aop.annotation.AuthUser;
import seoil.capstone.flashbid.global.common.response.ApiResult;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/auction")
public class AuctionPurchaseController {

    private final AuctionPurchaseService auctionPurchaseService;

    /**
     * 구매 확정 API
     * - ConfirmedBidsEntity 생성
     * - 판매자 포인트 지급
     * - 경매 상태 변경
     */
    @PostMapping("/purchase/confirm")
    @AuthUser
    public ApiResult<Void> confirmPurchase(
            Account account,
            @RequestBody PurchaseConfirmRequest request
    ) {
        log.info("구매 확정 요청 - 구매자: {}, 판매자: {}, 경매ID: {}, 금액: {}",
                request.getBuyerId(), request.getSellerId(), request.getAuctionId(), request.getAmount());

        try {
            auctionPurchaseService.confirmPurchase(account, request);
            return ApiResult.ok(null, "구매가 확정되었습니다.");
        } catch (IllegalArgumentException e) {
            log.warn("구매 확정 실패 - 유효성 검증 오류: {}", e.getMessage());
            return ApiResult.error(HttpStatus.CONFLICT, "", "");
        } catch (Exception e) {
            log.error("구매 확정 실패", e);
            return ApiResult.error(HttpStatus.CONFLICT, "", "");
        }
    }
}