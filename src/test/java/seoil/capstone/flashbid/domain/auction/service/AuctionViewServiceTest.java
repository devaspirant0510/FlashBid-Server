package seoil.capstone.flashbid.domain.auction.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import seoil.capstone.flashbid.domain.auction.dto.response.ViewCountIncreasedDto;
import seoil.capstone.flashbid.domain.auction.dto.response.ViewCountResultDto;
import seoil.capstone.flashbid.domain.auction.entity.AuctionViewCountEntity;
import seoil.capstone.flashbid.domain.auction.repository.jpa.AuctionRepository;
import seoil.capstone.flashbid.domain.auction.repository.jpa.BackUpAuctionViewCountRepository;
import seoil.capstone.flashbid.domain.auction.repository.redis.AuctionViewCountRepository;
import seoil.capstone.flashbid.domain.auction.repository.redis.ViewCountVerificationRepository;
import seoil.capstone.flashbid.domain.user.entity.Account;
import seoil.capstone.flashbid.global.common.error.NotFoundAuctionException;
import seoil.capstone.flashbid.global.core.provider.ClientIdentifierProvider;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuctionViewServiceTest {
    @InjectMocks
    private AuctionViewService auctionViewService;

    @Mock
    private AuctionViewCountRepository auctionViewCountRepository;
    @Mock
    private ViewCountVerificationRepository viewCountVerificationRepository;
    @Mock
    private BackUpAuctionViewCountRepository backUpAuctionViewCountRepository;
    @Mock
    private ClientIdentifierProvider clientIdentifierProvider;
    @Mock
    private AuctionRepository auctionRepository;

    @Test
    @DisplayName("로그인된 유저 조회수 증가")
    public void increaseAuthenticationUser() {
        // given
        Long auctionId = 1L;
        Account account = mock(Account.class);
        when(account.getId()).thenReturn(1L);
        MockHttpServletRequest request = new MockHttpServletRequest();

        // when
        when(viewCountVerificationRepository.isFirstView(auctionId, account.getId())).thenReturn(Boolean.TRUE);
        when(auctionRepository.existsById(auctionId)).thenReturn(true);

        // then
        ViewCountIncreasedDto result = auctionViewService.increaseView(auctionId, account, request);
        verify(auctionViewCountRepository).increase(auctionId);
        assertThat(result.isIncreased()).isEqualTo(Boolean.TRUE);
    }

    @Test
    @DisplayName("로그인 된 유저 조회수 어뷰징 방지")
    public void preventAuthenticatedUserDuplicateView() {
        // given
        Long auctionId = 1L;
        Account account = mock(Account.class);
        when(account.getId()).thenReturn(1L);
        MockHttpServletRequest request = new MockHttpServletRequest();

        // when
        when(viewCountVerificationRepository.isFirstView(auctionId, account.getId())).thenReturn(Boolean.TRUE, Boolean.FALSE);
        when(auctionRepository.existsById(auctionId)).thenReturn(true);

        auctionViewService.increaseView(auctionId, account, request);
        auctionViewService.increaseView(auctionId, account, request);
        // then
        verify(auctionViewCountRepository, times(1)).increase(auctionId);
    }

    @Test
    @DisplayName("비로그인 유저 조회수 증가")
    public void increaseViewCountWithAnonymous() {
        // given
        Long auctionId = 1L;
        Account account = null;
        MockHttpServletRequest request = new MockHttpServletRequest();

        // when
        String clientIp = "127.0.0.1";
        when(clientIdentifierProvider.extractClientIp(request)).thenReturn(clientIp);
        String clientAgent = "Chrome";
        when(clientIdentifierProvider.extractUserAgent(request)).thenReturn(clientAgent);
        when(auctionRepository.existsById(auctionId)).thenReturn(true);
        when(viewCountVerificationRepository.isFirstView(auctionId, clientIp,clientAgent)).thenReturn(Boolean.TRUE);

        auctionViewService.increaseView(auctionId, account, request);

        // then
        verify(viewCountVerificationRepository).isFirstView(auctionId, clientIp, clientAgent);
        verify(clientIdentifierProvider).extractClientIp(request);
        verify(clientIdentifierProvider).extractUserAgent(request);
        verify(auctionViewCountRepository).increase(auctionId);

    }

    @Test
    @DisplayName("유효한 경매 아이디인지 체크")
    public void validateAuction(){
        // given
        Long auctionId = 2L;
        Account account = mock(Account.class);
        MockHttpServletRequest request = new MockHttpServletRequest();

        // when
        when(auctionRepository.existsById(auctionId)).thenReturn(false);

        // then
        assertThrows(NotFoundAuctionException.class,()->{
            auctionViewService.increaseView(auctionId, account, request);
        });
    }

    @Test
    @DisplayName("redis 에 조회수가 있을때 조회수 리턴")
    public void getAuctionViewCountOnlyRedis(){
        // given
        Long auctionId = 1L;
        Long currentViewCount = 10L;

        // when
        when(auctionViewCountRepository.getViewCount(auctionId)).thenReturn(currentViewCount);


        ViewCountResultDto result = auctionViewService.getAuctionViewCount(auctionId);
        // then
        verify(auctionViewCountRepository).getViewCount(auctionId);
        verify(backUpAuctionViewCountRepository,never()).findById(auctionId);
        assertThat(result.getViewCount()).isEqualTo(currentViewCount);
    }

    @Test
    @DisplayName("redis 에 없고 백업 DB 에만 존재할때")
    public void notOnlyInRedisButAlsoExistInBackUpRDB(){
        // given
        Long auctionId = 1L;
        Long currentViewCount = 10L;

        // when
        when(auctionViewCountRepository.getViewCount(auctionId)).thenReturn(null);
        when(
                backUpAuctionViewCountRepository.findById(auctionId)
        ).thenReturn(Optional.of(AuctionViewCountEntity.builder().viewCount(currentViewCount).build()));

        ViewCountResultDto result = auctionViewService.getAuctionViewCount(auctionId);

        //then
        verify(backUpAuctionViewCountRepository).findById(auctionId);
        assertThat(result.getViewCount()).isEqualTo(currentViewCount);
    }

    @Test
    @DisplayName("redis 에도 없고 백업 DB 에도 없을때")
    public void neitherExistInRedisNorExistInBackUpRDB(){
        Long auctionId = 1L;
        Long currentViewCount = 10L;

        // when
        when(auctionViewCountRepository.getViewCount(auctionId)).thenReturn(null);
        when(
                backUpAuctionViewCountRepository.findById(auctionId)
        ).thenReturn(Optional.empty());
        when(
                backUpAuctionViewCountRepository.save(any(AuctionViewCountEntity.class))
        ).thenAnswer(invocation->invocation.getArgument(0));

        ViewCountResultDto result = auctionViewService.getAuctionViewCount(auctionId);

        //then
        verify(backUpAuctionViewCountRepository).findById(auctionId);
        verify(backUpAuctionViewCountRepository).save(any(AuctionViewCountEntity.class));
        assertThat(result.getViewCount()).isEqualTo(0);
    }


}