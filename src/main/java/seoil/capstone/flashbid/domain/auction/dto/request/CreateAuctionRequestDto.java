package seoil.capstone.flashbid.domain.auction.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import seoil.capstone.flashbid.global.common.enums.AuctionType;
import seoil.capstone.flashbid.global.common.enums.DeliveryType;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateAuctionRequestDto {
    private String title;
    private String description;
    private Integer startPrice;
    private Integer bidUnit;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private DeliveryType deliveryType;
}
