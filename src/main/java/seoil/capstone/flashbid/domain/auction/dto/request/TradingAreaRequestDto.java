package seoil.capstone.flashbid.domain.auction.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TradingAreaRequestDto {
    private Float latitude;
    private Float longitude;
    private Float radius;
    private String address;
}
