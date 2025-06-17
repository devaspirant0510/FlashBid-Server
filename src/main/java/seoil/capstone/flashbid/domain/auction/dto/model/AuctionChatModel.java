package seoil.capstone.flashbid.domain.auction.dto.model;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AuctionChatModel {
    private String contents;
    private String nickname;
    private String profileUrl;
    private Long userId;
    private AuctionBidModel bid;
}
