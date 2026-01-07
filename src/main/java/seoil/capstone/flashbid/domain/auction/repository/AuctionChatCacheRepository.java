package seoil.capstone.flashbid.domain.auction.repository;


import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class AuctionChatCacheRepository {
    private final RedisTemplate redisTemplate;
}
