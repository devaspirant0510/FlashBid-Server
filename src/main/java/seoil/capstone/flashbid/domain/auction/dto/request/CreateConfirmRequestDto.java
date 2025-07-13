package seoil.capstone.flashbid.domain.auction.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CreateConfirmRequestDto {
    private Long auctionId;
    private Long biddingLogId;
}
