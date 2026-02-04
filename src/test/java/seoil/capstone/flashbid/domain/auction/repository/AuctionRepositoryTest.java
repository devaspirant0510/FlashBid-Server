package seoil.capstone.flashbid.domain.auction.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import seoil.capstone.flashbid.domain.auction.dto.response.AuctionDetailDto;
import seoil.capstone.flashbid.domain.auction.entity.Auction;
import seoil.capstone.flashbid.domain.auction.entity.Goods;
import seoil.capstone.flashbid.domain.auction.projection.AuctionDetailProjection;
import seoil.capstone.flashbid.domain.auction.repository.jpa.AuctionRepository;
import seoil.capstone.flashbid.domain.auction.repository.jpa.GoodsRepository;
import seoil.capstone.flashbid.domain.category.entity.CategoryEntity;
import seoil.capstone.flashbid.domain.category.repository.CategoryRepository;
import seoil.capstone.flashbid.domain.user.entity.Account;
import seoil.capstone.flashbid.domain.user.repository.AccountRepository;
import seoil.capstone.flashbid.global.common.enums.*;
import seoil.capstone.flashbid.global.configuration.QueryDSLConfig;

@DataJpaTest
@Import(QueryDSLConfig.class)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AuctionRepositoryTest {
    @Autowired
    private AuctionRepository auctionRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private GoodsRepository goodsRepository;

    @Test
    @DisplayName("경매 상세조회 쿼리 테스트")
    @Transactional
    public void auctionInfoQueryTest(){
        Account account = Account.builder()
                .id(1L)
                .uuid("uuid")
                .userType(UserType.CUSTOMER)
                .userStatus(UserStatus.ACTIVE)
                .loginType(LoginType.EMAIL)
                .nickname("chodan")
                .password("chodan")
                .isVerified(true)
                .email("test@test.com")
                .description("test")
                .point(0)
                .build();
        accountRepository.save(account);
        Goods goods = Goods.builder()
                .title("title")
                .description("description")
                .deliveryType(DeliveryType.PARCEL)
                .build();
        goodsRepository.save(goods);
        CategoryEntity category = CategoryEntity.builder()
                .id(1L)
                .name("category")
                .root(null)
                .build();
        categoryRepository.save(category);
        Auction auction  = Auction.builder()
                .id(1L)
                .goods(goods)
                .user(account)
                .auctionStatus(AuctionStatus.BEFORE_START)
                .auctionType(AuctionType.LIVE)
                .bidUnit(0)
                .category(category)
                .startPrice(100)
                .build();
        auctionRepository.save(auction);

        AuctionDetailProjection result = auctionRepository.findAuctionDetailById(1L, 1L);
        AuctionDetailDto from = AuctionDetailDto.from(result, 10L);
        System.out.println(from);

    }


}