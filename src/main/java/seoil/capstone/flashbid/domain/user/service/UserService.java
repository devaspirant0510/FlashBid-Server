package seoil.capstone.flashbid.domain.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import seoil.capstone.flashbid.domain.auction.dto.response.AuctionDto;
import seoil.capstone.flashbid.domain.auction.entity.Auction;
import seoil.capstone.flashbid.domain.auction.entity.AuctionWishListCountEntity;
import seoil.capstone.flashbid.domain.auction.entity.BiddingLogEntity;
import seoil.capstone.flashbid.domain.auction.entity.ConfirmedBidsEntity;
import seoil.capstone.flashbid.domain.auction.repository.*;
import seoil.capstone.flashbid.domain.feed.dto.response.FeedDto;
import seoil.capstone.flashbid.domain.feed.repository.FeedRepository;
import seoil.capstone.flashbid.domain.feed.service.FeedService;
import seoil.capstone.flashbid.domain.file.dto.SaveFileDto;
import seoil.capstone.flashbid.domain.file.entity.FileEntity;
import seoil.capstone.flashbid.domain.file.service.FileService;
import seoil.capstone.flashbid.domain.user.dto.response.FollowUserDto;
import seoil.capstone.flashbid.domain.user.dto.response.UserDto;
import seoil.capstone.flashbid.domain.user.entity.Account;
import seoil.capstone.flashbid.domain.user.entity.FollowEntity;
import seoil.capstone.flashbid.domain.user.repository.AccountRepository;
import seoil.capstone.flashbid.domain.user.repository.FollowRepository;
import seoil.capstone.flashbid.global.common.enums.FileType;
import seoil.capstone.flashbid.global.common.error.ApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final AccountRepository accountRepository;
    private final FollowRepository followRepository;
    private final FeedRepository feedRepository;
    private final FeedService feedService;
    private final FileService fileService;
    private final ConfirmedBidsRepository confirmedBidsRepository;
    private final AuctionRepository auctionRepository;
    private final AuctionParticipateRepository auctionParticipateRepository;
    private final AuctionBidLogRepository auctionBidLogRepository;
    private final AuctionChatRepository auctionChatRepository;
    private final AuctionWishListCountRepository auctionWishListCountRepository;

    public UserDto getUserById(Account authUser, Long profileUserId){
        Account account = accountRepository.findById(profileUserId).orElseThrow(() ->
                new ApiException(HttpStatus.NOT_FOUND, "", "해당 유저를 찾을 수 없습니다."));

        UserDto userDto = getUserProfile(account);

        boolean isFollowing = false;
        if (authUser != null && !authUser.getId().equals(profileUserId)) {
            isFollowing = followRepository.existsByFollowerIdAndFollowingId(authUser.getId(), profileUserId);
        }

        userDto.setFollowing(isFollowing);
        return userDto;
    }

    @Transactional
    public UserDto getUserProfile(Account user) {
        int follower = followRepository.countByFollowingId(user.getId());
        int following = followRepository.countByFollowerId(user.getId());
        int feedCount = feedRepository.countByUserId(user.getId());

        List<FileEntity> allFiles = fileService.getAllFiles(user.getId(), FileType.PROFILE);
        FileEntity profileImage = null;
        if(!allFiles.isEmpty()){
            profileImage = allFiles.get(0);
        }
        log.info(user.getId().toString());
        log.info(allFiles.toString());
        return new UserDto(
                user,
                follower,
                following,
                feedCount,
                profileImage,
                false
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

    @Transactional
    public FileEntity uploadProfileImage(MultipartFile file,Account user){
        List<FileEntity> fileEntity = fileService.uploadAllFiles(List.of(file), user, user.getId(), FileType.PROFILE);
        user.setProfileUrl(fileEntity.get(0).getUrl());
        return fileEntity.get(0);
    }

    @Transactional(readOnly = true)
    public List<AuctionDto> getPurchaseHistory(Account user) {
        // 1. 사용자가 낙찰받은 내역 조회
        List<ConfirmedBidsEntity> purchases = confirmedBidsRepository.findAllByBidder_Id(user.getId());

        List<AuctionDto> auctionDtos = new ArrayList<>();

        // 2. 각 내역을 AuctionDto로 변환 (이미지 포함)
        for (ConfirmedBidsEntity confirmedBid : purchases) {
            Auction auction = confirmedBid.getAuction(); // 낙찰받은 경매 정보

            List<FileEntity> images = fileService.getAllFiles(auction.getGoods().getId(), FileType.GOODS);
            Integer participateCount = auctionParticipateRepository.countByAuctionId(auction.getId());
            Long biddingCount = auctionBidLogRepository.countByAuctionId(auction.getId());
            BiddingLogEntity bidHistory = auctionBidLogRepository.findTop1ByAuctionIdOrderByPriceDesc(auction.getId());

            // 낙찰 내역이므로, 확정된 낙찰가를 currentPrice로 사용
            Long currentPrice = confirmedBid.getBiddingLog() != null ? confirmedBid.getBiddingLog().getPrice() : auction.getStartPrice();

            Long chatCount = auctionChatRepository.countByAuctionId(auction.getId());

            AuctionWishListCountEntity wishListCountEntity = auctionWishListCountRepository.findById(auction.getId())
                    .orElse(AuctionWishListCountEntity.builder().count(0L).build());
            Long wishListCount = wishListCountEntity.getCount();

            auctionDtos.add(new AuctionDto(
                    auction,
                    images,
                    participateCount,
                    biddingCount,
                    currentPrice,
                    chatCount,
                    wishListCount
            ));
        }
        return auctionDtos;
    }

    @Transactional(readOnly = true)
    public List<AuctionDto> getSalesHistory(Account user) {
        // 1. 사용자가 등록한 모든 경매 조회
        List<Auction> userAuctions = auctionRepository.findAllByUserIdOrderByCreatedAtDesc(user.getId());

        List<AuctionDto> auctionDtos = new ArrayList<>();

        // 2. 각 경매를 AuctionDto로 변환 (AuctionService.queryAllAuction 로직 참고)
        for (Auction auction : userAuctions) {
            List<FileEntity> images = fileService.getAllFiles(auction.getGoods().getId(), FileType.GOODS);
            Integer participateCount = auctionParticipateRepository.countByAuctionId(auction.getId());
            Long biddingCount = auctionBidLogRepository.countByAuctionId(auction.getId());
            BiddingLogEntity bidHistory = auctionBidLogRepository.findTop1ByAuctionIdOrderByPriceDesc(auction.getId());

            // 현재 가격 (최고 입찰가 또는 시작가)
            Long currentPrice = (bidHistory != null) ? bidHistory.getPrice() : auction.getStartPrice();

            Long chatCount = auctionChatRepository.countByAuctionId(auction.getId());

            AuctionWishListCountEntity wishListCountEntity = auctionWishListCountRepository.findById(auction.getId())
                    .orElse(AuctionWishListCountEntity.builder().count(0L).build());
            Long wishListCount = wishListCountEntity.getCount();

            auctionDtos.add(new AuctionDto(
                    auction,
                    images,
                    participateCount,
                    biddingCount,
                    currentPrice,
                    chatCount,
                    wishListCount
            ));
        }
        return auctionDtos;
    }

    @Transactional(readOnly = true)
    public List<FollowUserDto> getFollowerList(Account authUser, Long userId) { // [수정] authUser 추가
        return followRepository.findAllByFollowingId(userId).stream()
                .map(followEntity -> {
                    Account itemUser = followEntity.getFollower();
                    boolean iFollowThem = (authUser != null) &&
                            followRepository.existsByFollowerIdAndFollowingId(authUser.getId(), itemUser.getId());
                    return FollowUserDto.from(itemUser, iFollowThem);
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<FollowUserDto> getFollowingList(Account authUser, Long userId) { // [수정] authUser 추가
        return followRepository.findAllByFollowerId(userId).stream()
                .map(followEntity -> {
                    Account itemUser = followEntity.getFollowing();

                    boolean iFollowThem = (authUser != null) &&
                            followRepository.existsByFollowerIdAndFollowingId(authUser.getId(), itemUser.getId());

                    if (authUser != null && Objects.equals(authUser.getId(), userId)) {
                        iFollowThem = true;
                    }

                    return FollowUserDto.from(itemUser, iFollowThem);
                })
                .collect(Collectors.toList());
    }
}
