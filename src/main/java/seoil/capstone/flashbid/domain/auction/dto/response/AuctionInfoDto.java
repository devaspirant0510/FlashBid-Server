package seoil.capstone.flashbid.domain.auction.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import seoil.capstone.flashbid.domain.auction.entity.Auction;
import seoil.capstone.flashbid.domain.auction.entity.BiddingLogEntity;
import seoil.capstone.flashbid.domain.file.entity.FileEntity;

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
}
