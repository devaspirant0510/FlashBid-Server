package com.choing.flashbid.infrastructure.kafka.event;


import lombok.Getter;
import com.choing.flashbid.global.common.util.DataSerializer;

import java.time.LocalDateTime;

@Getter
public class Event<T extends EventPayload> {
    private Long eventId;
    private EventType type;
    private T payload;
    private LocalDateTime timestamp;

    public static Event<EventPayload> of(Long eventId, EventType type, EventPayload payload) {
        Event<EventPayload> event = new Event<>();
        event.eventId = eventId;
        event.type = type;
        event.payload = payload;
        event.timestamp = LocalDateTime.now();
        return event;
    }

    public static Event<EventPayload> fromJson(String json) {
        EventEnvelope eventEnvelope = DataSerializer.deserialize(json, EventEnvelope.class);
        if (eventEnvelope == null) return null;
        Event<EventPayload> event = new Event<>();
        event.eventId = eventEnvelope.getEventId();
        event.timestamp = eventEnvelope.getTimestamp();
        event.type = EventType.valueOf(eventEnvelope.getType());
        event.payload = DataSerializer.deserialize(eventEnvelope.getPayload(), event.type.getPayloadClass());
        return event;
    }

    public String toJson() {
        return DataSerializer.serialize(this);
    }

    @Getter
    private static class EventEnvelope {
        private Long eventId;
        private String type;
        private Object payload;
        private LocalDateTime timestamp;
    }


}
