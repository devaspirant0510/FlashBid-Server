package seoil.capstone.flashbid.domain.admin.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import seoil.capstone.flashbid.domain.payment.entity.PointHistoryEntity;


@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/admin/payment")
public class AdminPaymentController {

    @GetMapping("/point-history")
    public Page<PointHistoryEntity> getPointHistoryList() {
        return null;
    }
}
