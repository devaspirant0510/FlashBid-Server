package com.choing.flashbid.domain.admin.dto.response;

import lombok.*;
import com.choing.flashbid.domain.admin.projection.AccountDashboardProjection;
import com.choing.flashbid.domain.admin.projection.AuctionDashboardProjection;
import com.choing.flashbid.domain.admin.projection.BiddingDashboardProjection;


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
