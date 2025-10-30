package seoil.capstone.flashbid.domain.payment.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseConfirmRequest {
    private Long auctionId;      // 경매 ID
    private Long buyerId;        // 구매자 ID
    private Long sellerId;       // 판매자 ID
    private Long amount;         // 낙찰가
    private Long roomId;         // DM 룸 ID
}