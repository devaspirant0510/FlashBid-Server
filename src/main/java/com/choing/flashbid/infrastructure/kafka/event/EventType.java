package com.choing.flashbid.infrastructure.kafka.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.choing.flashbid.infrastructure.kafka.event.payload.BidCreatedEventPayload;
import com.choing.flashbid.infrastructure.kafka.event.payload.ChatMessageEventPayload;

@RequiredArgsConstructor
@Slf4j
@Getter
public enum EventType {
    CHAT_MESSAGE(ChatMessageEventPayload.class,Topic.UNKNOWN_AUCTION_CHAT_MESSAGE),
    TRADE_CREATED(BidCreatedEventPayload.class,Topic.UNKNOWN_AUCTION_BID_CREATED);

    private final Class<? extends EventPayload> payloadClass;
    private final String topic;

    public static EventType from(String types){
        try{
            return valueOf(types);
        }catch (Exception e){
            log.error("Invalid event type '{}'", types);
            return null;
        }
    }
    public static class Topic{
        public static final String UNKNOWN_AUCTION_CHAT_MESSAGE = "unknown-auction-chat-message";
        public static final String UNKNOWN_AUCTION_BID_CREATED = "unknown-auction-bid-created";
    }
}
