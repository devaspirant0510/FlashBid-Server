package seoil.capstone.flashbid.domain.auction.dto.request;

import lombok.*;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CreateConfirmRequestDto {
    private Long auctionId;
    private Long biddingLogId;
}
