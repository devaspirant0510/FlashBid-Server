package com.choing.flashbid.infrastructure.kafka.event.payload;


import com.choing.flashbid.infrastructure.kafka.event.EventPayload;

import java.time.LocalDateTime;

public class ChatMessageEventPayload implements EventPayload {
    Long id;
    Long senderId;
    String message;
    Long auctionId;
    LocalDateTime createdAt;


}
