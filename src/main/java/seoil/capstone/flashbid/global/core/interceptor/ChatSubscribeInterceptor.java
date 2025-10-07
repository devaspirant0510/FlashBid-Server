package seoil.capstone.flashbid.global.core.interceptor;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;
import seoil.capstone.flashbid.domain.auction.repository.AuctionParticipateRepository;
import seoil.capstone.flashbid.domain.auth.service.AuthService;
import seoil.capstone.flashbid.domain.user.entity.Account;
import seoil.capstone.flashbid.global.common.error.ApiException;
import seoil.capstone.flashbid.global.core.provider.JwtProvider;

@Component
@RequiredArgsConstructor
public class ChatSubscribeInterceptor implements ChannelInterceptor {
    private final AuthService authService;
    private final JwtProvider jwtProvider;
    private final AuctionParticipateRepository auctionParticipateRepository;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        Account currentUser=null;

        assert accessor != null;
        if (accessor.getCommand() == StompCommand.CONNECT) {
            String authorization = accessor.getFirstNativeHeader("Authorization");
            String token = authorization.substring(7);
            System.out.println("token = " + token);
            currentUser = authService.authorizationTokenWithUser(token);
        }
        if(StompCommand.SUBSCRIBE.equals(accessor.getCommand())){

            String destination = accessor.getDestination(); // 예: /topic/chat/1234

            if (destination.startsWith("/topic/public/")) {
                Long roomId = Long.parseLong(destination.substring(destination.lastIndexOf("/") + 1));
//                boolean isJoined = auctionParticipateRepository.existsByAuctionIdAndParticipantId(roomId,currentUser.getId());
//                if(!isJoined){
//                    throw  new ApiException(HttpStatus.BAD_REQUEST,"","");
//                }
            }

        }

        // 유효성 검사
        return ChannelInterceptor.super.preSend(message, channel);
    }
}
