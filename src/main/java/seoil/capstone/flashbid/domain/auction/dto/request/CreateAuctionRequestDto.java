package seoil.capstone.flashbid.domain.auction.dto.request;


import lombok.*;
import seoil.capstone.flashbid.global.common.enums.DeliveryType;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CreateAuctionRequestDto {
    private String title;
    private String description;
    private Long categoryId;
    private Integer startPrice;
    private Integer bidUnit;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private DeliveryType deliveryType;
    private DeliveryInfoRequestDto deliveryInfo;
    private TradingAreaRequestDto tradingArea;
}
