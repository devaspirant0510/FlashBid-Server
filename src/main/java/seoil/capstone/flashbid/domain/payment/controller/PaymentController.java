package seoil.capstone.flashbid.domain.payment.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

    @PostMapping("/success")
    @AuthUser
    public ApiResult<PaymentEntity> paymentSuccess(
            Account account,
            @RequestBody PaymentSuccessDto dto,
            HttpServletRequest request
    ) {
        return ApiResult.ok(paymentService.processPaymentSuccess(account, dto), request);

    }
}
