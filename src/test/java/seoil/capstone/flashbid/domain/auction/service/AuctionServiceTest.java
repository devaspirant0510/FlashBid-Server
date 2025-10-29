package seoil.capstone.flashbid.domain.auction.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import seoil.capstone.flashbid.domain.auction.entity.Auction;
import seoil.capstone.flashbid.domain.auction.repository.*;
import seoil.capstone.flashbid.domain.category.repository.CategoryRepository;
import seoil.capstone.flashbid.domain.file.service.FileService;
import seoil.capstone.flashbid.domain.user.entity.Account;
import seoil.capstone.flashbid.domain.user.repository.AccountRepository;
import seoil.capstone.flashbid.global.common.enums.LoginType;
import seoil.capstone.flashbid.global.common.enums.UserStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AuctionServiceTest {
    @InjectMocks
    AuctionService service;

    FileService fileService;
    @Mock
    GoodsService goodsService;
    @Mock
    AuctionRepository auctionRepository;
    @Mock
    AuctionParticipateRepository auctionParticipateRepository;
    @Mock
    AuctionBidLogRepository auctionBidLogRepository;
    @Mock
    CategoryRepository categoryRepository;

    @Mock
    DeliveryInfoRepository deliveryInfoRepository;
    @Mock
    TradingAreaRepository tradingAreaRepository;
    @Mock
    ConfirmedBidsRepository confirmedBidsRepository;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    public void test(){
        List<String> list = new ArrayList<>();
        ArrayList<String> mock = Mockito.mock(ArrayList.class);

    }

    @Test
    @DisplayName("sss")
    void testDB(){
        Mockito.when(auctionRepository.findById(1L)).thenReturn(Optional.of(Auction.builder().id(1L).build()));
        Assertions.assertThat(service.getAuctionById(1L)).isEqualTo(Auction.builder().id(1L).build());
    }

    @Test
    void confirmedBidsEntity() {
    }

    @Test
    void getAuctionById() {
    }

    @Test
    void saveAuction() {
    }

    @Test
    void getAuctionInfoByIdToDto() {
    }

    @Test
    void getRecomendAuction() {
    }

    @Test
    void queryAllAuction() {
    }

    @Test
    void participateUser() {
    }

    @Test
    void testParticipateUser() {
    }
}