package com.choing.flashbid.domain.auction.repository.redis;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.choing.flashbid.domain.auction.repository.jpa.BackUpAuctionViewCountRepository;
import com.choing.flashbid.global.base.BaseRedisTemplate;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class AuctionViewCountRepository extends BaseRedisTemplate {
    private static final String KEY_FORMAT = "auction:view:count";
    private static final Integer BACKUP_THRESHOLD = 100;
    private final StringRedisTemplate redisTemplate;
    private final BackUpAuctionViewCountRepository backUpAuctionViewCountRepository;


    @Transactional
    public void increase(Long auctionId) {
        Long currentView = getViewCount(auctionId);
        if (currentView!=null && currentView % BACKUP_THRESHOLD == 0) {
            backUpAuctionViewCountRepository.updateViewCountAuctionId(auctionId, currentView);
        }
        redisTemplate.opsForHash().increment(generateKey(), auctionId.toString(),1);
    }

    public Long getViewCount(Long auctionId) {
        // 레디스에 조회수 정보 존재시 리턴
        Object viewCount = redisTemplate.opsForHash().get(generateKey(),auctionId.toString());
        return viewCount == null ? null : Long.parseLong(viewCount.toString());
    }

    public List<Long> getKeysByAuctionView(){
        return redisTemplate.keys(generateKey()).stream()
                .map(Object::toString)
                .map(Long::valueOf)
                .toList();
    }

    public List<Long> getViewCounts(List<Long> auctionIds) {
        List<Object> results = redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            StringRedisConnection stringRedisConn = (StringRedisConnection) connection;

            for (Long auctionId : auctionIds) {
                stringRedisConn.hGet(generateKey(),auctionId.toString());
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
