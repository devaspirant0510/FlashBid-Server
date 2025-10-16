package seoil.capstone.flashbid.domain.payment.dto;

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
public class BidDto {
    private Integer amount;
    private Long auctionId;
}
