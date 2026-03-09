package com.choing.flashbid.domain.admin.projection;

public interface AuctionDashboardProjection {
    Long getActiveAuctionCount();
    Long getTodayAuctionCount();
    Long getYesterdayAuctionCount();
}