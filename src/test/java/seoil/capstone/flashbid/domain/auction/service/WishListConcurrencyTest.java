package seoil.capstone.flashbid.domain.auction.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import seoil.capstone.flashbid.domain.auction.dto.response.AuctionDetailDto;
import seoil.capstone.flashbid.domain.auction.entity.Auction;
import seoil.capstone.flashbid.domain.auction.entity.AuctionWishListCountEntity;
import seoil.capstone.flashbid.domain.auction.repository.jpa.AuctionRepository;
import seoil.capstone.flashbid.domain.auction.repository.jpa.AuctionWishListCountRepository;
import seoil.capstone.flashbid.domain.auction.repository.jpa.AuctionWishListRepository;
import seoil.capstone.flashbid.domain.category.entity.CategoryEntity;
import seoil.capstone.flashbid.domain.category.repository.CategoryRepository;
import seoil.capstone.flashbid.domain.user.entity.Account;
import seoil.capstone.flashbid.domain.user.repository.AccountRepository;
import seoil.capstone.flashbid.util.TestUtilFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class WishListConcurrencyTest {
    @Autowired
    private AuctionService auctionService;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AuctionRepository auctionRepository;
    private TestUtilFactory testUtilFactory;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private AuctionWishListCountRepository auctionWishListCountRepository;
    @Autowired
    private AuctionWishListService auctionWishListService;
    @Autowired
    private AuctionWishListRepository auctionWishListRepository;


    @BeforeEach
    public  void testDataInit(){
        testUtilFactory = new TestUtilFactory();
        CategoryEntity category = categoryRepository.save(
                CategoryEntity.builder()
                        .root(null)
                        .name("category")
                        .build()
        );
        categoryRepository.save(category);
        for (long i = 0; i < 500; i++) {
            Account account = testUtilFactory.makeUser();
            accountRepository.save(account);
            Auction auction = testUtilFactory.makeAuction(i, account, category);
            auctionRepository.save(auction);
        }
        System.out.println("===================================================");
    }

    @Test
    public void simpleAddWishListTest() {
        // given
        long auctionId = 1L;
        Account account = getAccount(auctionId);
        // when
        auctionService.addWishList(account, auctionId);
        AuctionDetailDto result = auctionService.getAuctionDetail(auctionId, account);
        // then
        assertThat(result.getIsLiked()).isEqualTo(true);
    }

    @Test
    @DisplayName("특정 옥션에 위시리스트 추가 동시성 테스트 락을 적용 x")
    public void concurrencyAddWishListTestWithNoLock() throws InterruptedException {
        long startTime = System.currentTimeMillis();
        ExecutorService executor = Executors.newFixedThreadPool(100);
        CountDownLatch latch = new CountDownLatch(500);
        long auctionId = 1L;

        for (long i= 1; i <= 500; i++) {
            final long id=i;
            executor.execute(() -> {
                try{
                    auctionService.addWishList(getAccount(id),auctionId);
                }catch (Exception e){
                    System.out.println(e.getMessage());

                }finally {
                    latch.countDown();
                }
            });

        }
        long endTime = System.currentTimeMillis(); // 끝난 시간
        long elapsedTime = endTime - startTime;
        latch.await();
        executor.shutdown();

        AuctionWishListCountEntity result = auctionWishListCountRepository.findById(auctionId).get();
        // then
        assertThat(result.getCount()).isNotEqualTo(500);

        System.out.println("Actual quantity to be applied : "+500);
        System.out.println("Quantity actually applied     : "+result.getCount());
        System.out.println("Success rate                  : "+(result.getCount()/500f *100)+"%");
        System.out.println("Elapsed time                  : " + elapsedTime + "ms");
    }
    @Test
    @DisplayName("Pessimistic 락 쿼리 테스트")
    @Transactional
    public void lockTest(){
        // given
        long accountId1 = 1L;
        Account account1 = getAccount(accountId1);
        long accountId2 = 2L;
        Account account2 = getAccount(accountId2);

        // when
        auctionWishListService.increase(account1, 1L);
        auctionWishListService.increase(account2, 1L);
        System.out.println("------1");
        System.out.println(auctionWishListRepository.findAll());
        System.out.println(auctionWishListCountRepository.findAll());
        auctionWishListService.decrease(account1, 1L);
        Long result = auctionWishListCountRepository.findById(1L).orElseThrow().getCount();

        // then
        assertThat(result).isEqualTo(1);

    }

    @Test
    @DisplayName("특정 옥션에 위시리스트 추가 동시성 테스트 락을 적용 x")
    public void concurrencyAddWishListTestWithPessimisticLock() throws InterruptedException {
        long startTime = System.currentTimeMillis();
        ExecutorService executor = Executors.newFixedThreadPool(20);
        CountDownLatch latch = new CountDownLatch(500);
        long auctionId = 1L;

        latch.countDown();
        for (long i= 1; i <= 500; i++) {
            final long id=i;
            executor.execute(() -> {
                try{
                    auctionWishListService.increase(getAccount(id),auctionId);
                }catch (Exception e){
                    e.printStackTrace();
                    System.out.println(e.getMessage());
                    throw e;

                }finally {
                    latch.countDown();
                }
            });

        }
        long endTime = System.currentTimeMillis(); // 끝난 시간
        long elapsedTime = endTime - startTime;
        latch.await();
        executor.shutdown();

        AuctionWishListCountEntity result = auctionWishListCountRepository.findById(auctionId).get();
        // then
        assertThat(result.getCount()).isEqualTo(500);

        System.out.println("Actual quantity to be applied : "+500);
        System.out.println("Quantity actually applied     : "+result.getCount());
        System.out.println("Success rate                  : "+(result.getCount()/500f *100)+"%");
        System.out.println("Elapsed time                  : " + elapsedTime + "ms");
    }





    private Account getAccount(Long id) {
        return accountRepository.findById(id).get();
    }

}
