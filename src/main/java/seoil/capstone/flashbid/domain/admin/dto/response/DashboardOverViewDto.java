package seoil.capstone.flashbid.domain.admin.dto.response;

import lombok.*;
import seoil.capstone.flashbid.domain.admin.projection.AccountDashboardProjection;
import seoil.capstone.flashbid.domain.admin.projection.AuctionDashboardProjection;
import seoil.capstone.flashbid.domain.admin.projection.BiddingDashboardProjection;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DashboardOverViewDto {
    private AuctionDashboardProjection liveAuctionStats;
    private AuctionDashboardProjection blindAuctionStats;

    private BiddingDashboardProjection liveBiddingDashboardStats;
    private BiddingDashboardProjection blindBiddingDashboardStats;

    private AccountDashboardProjection accountDashboardStats;

}
