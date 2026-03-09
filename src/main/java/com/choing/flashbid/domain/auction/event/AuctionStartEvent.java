package com.choing.flashbid.domain.auction.event;

import com.choing.flashbid.global.common.enums.AuctionType;

public record AuctionStartEvent(Long auctionId, AuctionType auctionType) {
}
