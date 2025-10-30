package seoil.capstone.flashbid.domain.auction.repository;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import seoil.capstone.flashbid.global.common.enums.AuctionType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Repository
@RequiredArgsConstructor
@Slf4j
public class AuctionEventRepository {
    private final RedisTemplate<String, String> redisTemplate;
    private static final String LIVE_PREFIX_START = "auction:live:start:";
    private static final String LIVE_PREFIX_END = "auction:live:end:";
    private static final String BLIND_PREFIX_START = "auction:blind:start:";
    private static final String BLIND_PREFIX_END = "auction:blind:end:";

    /**
     * 경매 시작/종료 TTL 등록
     */
    public void registerAuctionTTLs(Long auctionId, AuctionType auctionType, LocalDateTime startAt, LocalDateTime endAt) {
        LocalDateTime now = LocalDateTime.now();
        log.info("Registering auction TTLs - Auction ID: {}, Type: {}, Start At: {}, End At: {}, Now: {}",
                auctionId, auctionType, startAt, endAt, now);

        long startTtl = ChronoUnit.SECONDS.between(now, startAt);
        long endTtl = ChronoUnit.SECONDS.between(now, endAt);

        String startKey = auctionType == AuctionType.LIVE ? LIVE_PREFIX_START : BLIND_PREFIX_START;
        String endKey = auctionType == AuctionType.LIVE ? LIVE_PREFIX_END : BLIND_PREFIX_END;

        if (startTtl > 0) {
            redisTemplate.opsForValue().set(startKey + auctionId, "START", Duration.ofSeconds(startTtl));
        }

        if (endTtl > 0) {
            redisTemplate.opsForValue().set(endKey + auctionId, "END", Duration.ofSeconds(endTtl));
        }
    }
}
