package seoil.capstone.flashbid.domain.dm.dto;

import lombok.*;
import seoil.capstone.flashbid.domain.dm.entity.DMRoom;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DMRoomDetailDto {
    private Long roomId;
    private String roomName;

    // 경매 정보
    private AuctionDetailDto auction;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AuctionDetailDto {
        private Long id;
        private String goodsTitle;
        private Integer startPrice;
        private String endTime;
        private String imageUrl;  // ✅ 이미지 URL 추가
    }

    public static DMRoomDetailDto from(DMRoom room) {
        AuctionDetailDto auctionDto = null;

        if (room.getAuction() != null) {
            // 상품 이미지 조회 (첫 번째 이미지)
            String imageUrl = "";
            if (room.getAuction().getGoods() != null) {
                // goods의 파일을 조회해야 함
                // 여기서는 나중에 FileRepository를 통해 조회
                imageUrl = "";
            }

            auctionDto = AuctionDetailDto.builder()
                    .id(room.getAuction().getId())
                    .goodsTitle(room.getAuction().getGoods() != null ?
                            room.getAuction().getGoods().getTitle() : "")
                    .startPrice(room.getAuction().getStartPrice())
                    .endTime(room.getAuction().getEndTime() != null ?
                            room.getAuction().getEndTime().toString() : "")
                    .imageUrl(imageUrl)
                    .build();
        }

        return DMRoomDetailDto.builder()
                .roomId(room.getId())
                .roomName(room.getRoomName())
                .auction(auctionDto)
                .build();
    }
}