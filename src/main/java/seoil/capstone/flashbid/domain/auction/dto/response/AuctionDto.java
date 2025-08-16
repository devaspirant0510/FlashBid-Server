package seoil.capstone.flashbid.domain.auction.dto.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import seoil.capstone.flashbid.domain.auction.entity.Auction;
import seoil.capstone.flashbid.domain.file.entity.FileEntity;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuctionDto {
    private Auction auction;
    private List<FileEntity> images;
    private Integer participateCount;
    private Long currentPrice;
    private Long chatMessagingCount;
    private Long wishListCount;


}
