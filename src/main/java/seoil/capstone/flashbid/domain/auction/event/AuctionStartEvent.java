package seoil.capstone.flashbid.domain.auction.event;

import seoil.capstone.flashbid.global.common.enums.AuctionType;

public record AuctionStartEvent(Long auctionId, AuctionType auctionType) {
}
