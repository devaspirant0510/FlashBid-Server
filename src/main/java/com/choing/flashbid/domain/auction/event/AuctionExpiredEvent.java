package com.choing.flashbid.domain.auction.event;


import com.choing.flashbid.global.common.enums.AuctionType;

public record AuctionExpiredEvent(Long auctionId, AuctionType auctionType) {}