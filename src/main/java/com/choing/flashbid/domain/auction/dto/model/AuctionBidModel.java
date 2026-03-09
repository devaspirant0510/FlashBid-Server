package com.choing.flashbid.domain.auction.dto.model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuctionBidModel {
    private Long price;
    private Long prevPrice;
}
