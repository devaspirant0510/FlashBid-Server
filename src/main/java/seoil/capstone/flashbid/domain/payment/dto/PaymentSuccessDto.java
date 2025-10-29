package seoil.capstone.flashbid.domain.payment.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PaymentSuccessDto {
    private String paymentKey;
    private String orderId;
    private String receiptId;
    private String receiptUrl;
    private String status;
    private String userId;
    private Integer pointAmount;
    private Integer paymentAmount;
    private String method;
    private LocalDateTime purchaseAt;
}
