package seoil.capstone.flashbid.util;


import seoil.capstone.flashbid.domain.auction.entity.Auction;
import seoil.capstone.flashbid.domain.category.entity.CategoryEntity;
import seoil.capstone.flashbid.domain.user.entity.Account;
import seoil.capstone.flashbid.global.common.enums.*;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TestUtilFactory {

    private static final AtomicLong SEQ = new AtomicLong(1L);

    public Account makeUser() {
        return makeUser(SEQ.getAndIncrement());
    }

    public Account makeUser(Long id) {
        Long safeId = (id == null) ? SEQ.getAndIncrement() : id;

        return Account.builder()
                .id(safeId)
                .loginType(LoginType.EMAIL)
                .userStatus(UserStatus.ACTIVE)
                .userType(UserType.CUSTOMER)
                .email("user" + safeId + "@example.com")
                .password("password")
                .isVerified(true)
                .uuid(UUID.randomUUID().toString())
                .nickname("user-" + safeId)
                .description("test user")
                .profileUrl("https://example.com/profile.png")
                .point(0)
                .build();
    }

//    public Auction makeAuction(CategoryEntity category) {
//        return makeAuction(SEQ.getAndIncrement(), category);
//    }

    public Auction makeAuction(Long id, Account user, CategoryEntity category) {
        Long safeId = (id == null) ? SEQ.getAndIncrement() : id;
        LocalDateTime now = LocalDateTime.now();

        return Auction.builder()
                .id(safeId)
                .user(user)
                .category(category)
                .startTime(now.minusMinutes(10))
                .endTime(now.plusMinutes(20))
                .startPrice(1000)
                .bidUnit(100)
                .count(0)
                .auctionType(AuctionType.LIVE)
                .auctionStatus(AuctionStatus.BEFORE_START)
                .build();
    }

}
