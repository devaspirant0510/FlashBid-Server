package seoil.capstone.flashbid.domain.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import seoil.capstone.flashbid.domain.feed.dto.response.FeedDto;
import seoil.capstone.flashbid.domain.feed.repository.FeedRepository;
import seoil.capstone.flashbid.domain.feed.service.FeedService;
import seoil.capstone.flashbid.domain.user.dto.response.UserDto;
import seoil.capstone.flashbid.domain.user.entity.Account;
import seoil.capstone.flashbid.domain.user.entity.FollowEntity;
import seoil.capstone.flashbid.domain.user.repository.AccountRepository;
import seoil.capstone.flashbid.domain.user.repository.FollowRepository;
import seoil.capstone.flashbid.global.common.error.ApiException;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final AccountRepository accountRepository;
    private final FollowRepository followRepository;
    private final FeedRepository feedRepository;
    private final FeedService feedService;

    @Transactional
    public UserDto getUserProfile(Account user) {
        int follower = followRepository.countByFollowerId(user.getId());
        int following = followRepository.countByFollowingId(user.getId());
        int feedCount = feedRepository.countByUserId(user.getId());
        return new UserDto(
                user,
                follower,
                following,
                feedCount
        );
    }

    @Transactional
    public FollowEntity followUser(Account my, Long followUserId) {
        // TODO 팔로워 했는지 유효성 검사
        Account followUser = accountRepository.findById(followUserId).orElseThrow(() ->
                new ApiException(HttpStatus.NOT_FOUND, "404E00U000", "존재 하지 않는 유저입니다.")

        );
        FollowEntity follow = FollowEntity.builder()
                .follower(my)
                .following(followUser)
                .build();
        return followRepository.save(follow);
    }

    @Transactional
    public Boolean unFollowUser(Account my, Long unFollowerId){
        int result = followRepository.deleteByFollowerIdAndFollowingId(my.getId(),unFollowerId);
        return  result==1;
    }

    @Transactional
    public List<FeedDto> getAllFeedByUserId(Long userId){
        List<FeedDto> feedDtos = new ArrayList<>();
        feedRepository.findAllByUserId(userId).forEach(feed->{
            feedDtos.add(feedService.getQueryFeedDto(feed));
        });
        return feedDtos;
    }
}
