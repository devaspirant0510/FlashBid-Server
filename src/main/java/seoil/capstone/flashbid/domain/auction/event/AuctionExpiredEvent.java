package seoil.capstone.flashbid.domain.auction.event;


import seoil.capstone.flashbid.global.common.enums.AuctionType;

public record AuctionExpiredEvent(Long auctionId, AuctionType auctionType) {}