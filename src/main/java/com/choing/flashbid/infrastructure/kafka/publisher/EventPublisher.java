package com.choing.flashbid.infrastructure.kafka.publisher;


import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import com.choing.flashbid.global.common.util.DataSerializer;
import com.choing.flashbid.infrastructure.id.SnowflakeGenerator;
import com.choing.flashbid.infrastructure.kafka.event.EventPayload;
import com.choing.flashbid.infrastructure.kafka.event.EventType;

@RequiredArgsConstructor
@Component
public class EventPublisher {
    private final SnowflakeGenerator snowflake;
    private final KafkaTemplate<String, String> kafkaTemplate;


    private final ApplicationEventPublisher applicationEventPublisher;

    public void publish(EventType eventType, EventPayload eventPayload) {
//        applicationEventPublisher.publishEvent(Event.of(snowflake.nextId(),eventType,eventPayload));
        kafkaTemplate.send(
                eventType.getTopic(),
                DataSerializer.serialize(eventPayload)
        );
    }

}
