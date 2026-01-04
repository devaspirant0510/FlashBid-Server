package seoil.capstone.flashbid.domain.admin.projection;

public interface AuctionDashboardProjection {
    Long getActiveAuctionCount();
    Long getTodayAuctionCount();
    Long getYesterdayAuctionCount();
}