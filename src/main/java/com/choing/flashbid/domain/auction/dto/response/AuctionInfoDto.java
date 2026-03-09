package com.choing.flashbid.domain.auction.dto.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.choing.flashbid.domain.auction.entity.Auction;
import com.choing.flashbid.domain.auction.entity.BiddingLogEntity;
import com.choing.flashbid.domain.file.entity.FileEntity;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuctionInfoDto {
    private Auction auction;
    private List<FileEntity> images;
    private Integer participateCount;
    private Long biddingCount;
    private BiddingLogEntity lastBiddingLog;
    private Long wishListCount;
    @JsonProperty("isWishListed")
    private boolean isWishListed;
}
