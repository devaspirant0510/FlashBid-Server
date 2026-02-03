package seoil.capstone.flashbid.domain.auction.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import seoil.capstone.flashbid.domain.auction.projection.AuctionProjection;
import seoil.capstone.flashbid.domain.auction.repository.jpa.AuctionRepository;
import seoil.capstone.flashbid.domain.auction.service.AuctionService;

@SpringBootTest
class AuctionRepositoryTest {
    @Autowired
    private AuctionRepository auctionRepository;
    @Autowired
    private AuctionService auctionService;

    @Test
    void findAllByAuctionPageV2() {
//        List<AuctionProjection> allByAuctionPageV2 = auctionRepository.findAllByAuctionPageV2(1L,10,10);
//        System.out.println(allByAuctionPageV2);
//        allByAuctionPageV2.forEach(v->{
//            System.out.println(v.getBidderName());
//        });
//        assertThat(allByAuctionPageV2.size()).isEqualTo(10);
    }

    @Test
    void findByAuctionPageModel(){
        Page<AuctionProjection> auctionProjectionPagingModel = auctionService.searchAuction(
                "test",
                0,
                1,
                10,
                10
        );
        System.out.println(auctionProjectionPagingModel);
        auctionProjectionPagingModel.getContent().forEach(v->{
            System.out.println(v.getBidderName());
            System.out.println(v.getGoodsTitle());
        });

    }
}