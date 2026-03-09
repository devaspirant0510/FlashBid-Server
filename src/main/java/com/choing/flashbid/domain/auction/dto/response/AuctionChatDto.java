package com.choing.flashbid.domain.auction.dto.response;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import com.choing.flashbid.domain.auction.entity.AuctionChatEntity;
import com.choing.flashbid.domain.auction.projection.AuctionChatProjection;
import com.choing.flashbid.global.common.enums.ChatType;

import java.time.LocalDateTime;


@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AuctionChatDto implements AuctionChatProjection {
    private Long id;
    private String contents;
    private LocalDateTime createdAt;
    private Long userId;
    private String nickname;
    private String profileUrl;
    private ChatType chatType;
    private Long prevPrice;
    private Long price;

    public static AuctionChatDto fromEntity(AuctionChatEntity entity) {
        return new AuctionChatDto(
                entity.getId(),
                entity.getContents(),
                entity.getCreatedAt(),
                entity.getUser().getId(),
                entity.getUser().getNickname(),
                entity.getUser().getProfileUrl(),
                entity.getChatType(),
                entity.getBiddingLog() == null ? null : entity.getBiddingLog().getPrevPrice(),
                entity.getBiddingLog() == null ? null : entity.getBiddingLog().getPrice()
        );
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getContents() {
        return contents;
    }

    @Override
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public Long getUserId() {
        return userId;
    }

    @Override
    public String getNickname() {
        return nickname;
    }

    @Override
    public String getProfileUrl() {
        return profileUrl;
    }

    @Override
    public ChatType getChatType() {
        return chatType;
    }

    @Override
    public Long getPrevPrice() {
        return prevPrice;
    }

    @Override
    public Long getPrice() {
        return price;
    }
}
