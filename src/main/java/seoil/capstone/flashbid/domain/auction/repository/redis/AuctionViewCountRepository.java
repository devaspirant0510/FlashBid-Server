package seoil.capstone.flashbid.domain.auction.repository.redis;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;
import seoil.capstone.flashbid.global.base.BaseRedisTemplate;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class AuctionViewCountRepository extends BaseRedisTemplate {
    private static final String KEY_FORMAT = "auction:%s:view:count";
    private final StringRedisTemplate redisTemplate;

    public void increase(Long auctionId) {
        Long currentView = getViewCount(auctionId);
        redisTemplate.opsForValue().set(generateKey(auctionId), currentView==null?"1":currentView.toString());
    }

    public Long getViewCount(Long auctionId) {
        // 레디스에 조회수 정보 존재시 리턴
        String viewCount = redisTemplate.opsForValue().get(generateKey(auctionId));
        return viewCount == null ? null : Long.parseLong(viewCount);
    }

    public List<Long> getViewCounts(List<Long> auctionIds) {
        List<Object> results = redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            StringRedisConnection stringRedisConn = (StringRedisConnection) connection;

            for (Long auctionId : auctionIds) {
                String key = String.format(generateKey(auctionId), auctionId);
                stringRedisConn.get(key);
            }
            return null;
        });

        return results.stream()
                .map(obj -> {
                    if (obj == null) return 0L;
                    return Long.parseLong(obj.toString());
                })
                .toList();
    }

    @Override
    public String getKeyFormate() {
        return KEY_FORMAT;
    }

}
