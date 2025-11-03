package seoil.capstone.flashbid.domain.auction.projection;

import seoil.capstone.flashbid.global.common.enums.ChatType;

import java.time.LocalDateTime;

public interface AuctionChatProjection {
    Long getId();

    String getContents();

    LocalDateTime getCreatedAt();

    Long getUserId();

    String getNickname();

    String getProfileUrl();

    ChatType getChatType();

    Long getPrevPrice();

    Long getPrice();
}
