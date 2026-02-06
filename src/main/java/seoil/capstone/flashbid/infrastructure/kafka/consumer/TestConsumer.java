package seoil.capstone.flashbid.infrastructure.kafka.consumer;


import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import seoil.capstone.flashbid.infrastructure.kafka.event.EventType;

@Component
@Slf4j
public class TestConsumer {

    @KafkaListener(topics = {
            EventType.Topic.UNKNOWN_AUCTION_BID_CREATED,
            EventType.Topic.UNKNOWN_AUCTION_CHAT_MESSAGE
    })
    public void listen(String message, Acknowledgment ack) {
        log.info("Received message='{}'", message);
        ack.acknowledge();
    }
}
