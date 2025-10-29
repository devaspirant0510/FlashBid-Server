package seoil.capstone.flashbid.domain.payment.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import seoil.capstone.flashbid.domain.payment.dto.PaymentSuccessDto;
import seoil.capstone.flashbid.domain.payment.entity.PaymentEntity;
import seoil.capstone.flashbid.domain.payment.service.PaymentService;
import seoil.capstone.flashbid.domain.user.entity.Account;
import seoil.capstone.flashbid.global.aop.annotation.AuthUser;
import seoil.capstone.flashbid.global.common.response.ApiResult;


@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/payment")
public class PaymentController {
    private final PaymentService paymentService;

    @GetMapping("/last-bid/{id}")
    @AuthUser
    public ApiResult<Long> lastBidPrice(
            Account account,
            @PathVariable("id") Long auctionId
    ) {
        Long lastBidPrice = paymentService.getLastBidPrice(account, auctionId);
        return ApiResult.ok(
                lastBidPrice,
                lastBidPrice == 0 ? "입찰 내역이 없습니다." : "마지막 입찰가는 " + lastBidPrice + "p 입니다."
        );
    }

    @GetMapping("/point-history/check-purchase/{auctionId}")
    public ApiResult<Boolean> checkPurchaseConfirmed(
            @PathVariable("auctionId") Long auctionId
    ) {
        log.info("구매 확정 상태 확인 - 경매ID: {}", auctionId);

        boolean isPurchased = paymentService.isPurchaseConfirmed(auctionId);

        return isPurchased
                ? ApiResult.ok(true, "구매가 확정되었습니다.")
                : ApiResult.ok(false, "아직 구매가 확정되지 않았습니다.");
    }

    @PostMapping("/success")
    @AuthUser
    public ApiResult<PaymentEntity> paymentSuccess(
            Account account,
            @RequestBody PaymentSuccessDto dto,
            HttpServletRequest request
    ) {
        return ApiResult.ok(paymentService.processPaymentSuccess(account, dto));

    }
}
