package com.choing.flashbid.domain.admin.projection;


public interface BiddingDashboardProjection {
    Long getTotalSales();
    Long getTodaySales();
    Long getYesterdaySales();

    Long getTotalConfirmedCount();
    Long getTodayConfirmedCount();
    Long getYesterdayConfirmedCount();
}
