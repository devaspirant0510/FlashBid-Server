package seoil.capstone.flashbid.infrastructure.redis;


import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;
import seoil.capstone.flashbid.domain.auction.event.AuctionExpiredEvent;
import seoil.capstone.flashbid.domain.auction.event.AuctionStartEvent;
import seoil.capstone.flashbid.global.common.enums.AuctionType;

@Component
public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener {
    private final ApplicationEventPublisher eventPublisher;

    public RedisKeyExpirationListener(RedisMessageListenerContainer listenerContainer,
                                      ApplicationEventPublisher eventPublisher) {
        super(listenerContainer);
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String key = message.toString();
        // 예: auction:1234
        if (key.startsWith("auction:live:end:")) {
            Long auctionId = Long.valueOf(key.split(":")[3]);
            eventPublisher.publishEvent(new AuctionExpiredEvent(auctionId, AuctionType.LIVE));
        } else if (key.startsWith("auction:blind:end:")) {
            Long auctionId = Long.valueOf(key.split(":")[3]);
            eventPublisher.publishEvent(new AuctionStartEvent(auctionId, AuctionType.BLIND));
        } else if (key.startsWith("auction:live:start:")) {
            Long auctionId = Long.valueOf(key.split(":")[3]);
            eventPublisher.publishEvent(new AuctionStartEvent(auctionId, AuctionType.LIVE));
        } else if (key.startsWith("auction:blind:start:")) {
            Long auctionId = Long.valueOf(key.split(":")[3]);
            eventPublisher.publishEvent(new AuctionStartEvent(auctionId, AuctionType.BLIND));
        }
    }
}
