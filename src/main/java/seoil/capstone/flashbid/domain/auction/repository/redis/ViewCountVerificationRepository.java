package seoil.capstone.flashbid.domain.auction.repository.redis;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;
import seoil.capstone.flashbid.global.base.BaseRedisTemplate;

import java.time.Duration;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ViewCountVerificationRepository extends BaseRedisTemplate {
    private final static int AGG_VIEW_COUNT_MINUTE = 5;
    private final StringRedisTemplate redisTemplate;
    @Override
    public String getKeyFormate() {
        return "auction-view:%s:user:%s";
    }

    public boolean isFirstView(
            Long auctionId,
            Long userId
    ) {
        String redisKey = generateKey(auctionId, userId);
        return setIfAbsent(redisKey);
    }
    public boolean isFirstView(
            Long auctionId,
            String ip,
            String userAgent
    ) {
        String viewerKey = DigestUtils.sha256Hex(ip + ":" + userAgent).substring(0,16);
        String redisKey = generateKey(auctionId, viewerKey);
        return setIfAbsent(redisKey);
    }
    private boolean setIfAbsent(String key) {
        Boolean result = redisTemplate.opsForValue()
                .setIfAbsent(key, "", Duration.ofMinutes(AGG_VIEW_COUNT_MINUTE));
        return Boolean.TRUE.equals(result);
    }
}
