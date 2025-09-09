package seoil.capstone.flashbid.domain.auction.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import seoil.capstone.flashbid.domain.auction.entity.BiddingLogEntity;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AuctionBidLogRepositoryTest {
    @Autowired
    AuctionBidLogRepository repository;

    @BeforeEach
    void setUp() {
    }

    @Test
    void test(){

    }
}