package seoil.capstone.flashbid.domain.auction.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

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