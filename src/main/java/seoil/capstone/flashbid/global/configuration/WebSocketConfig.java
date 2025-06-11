package seoil.capstone.flashbid.global.configuration;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import seoil.capstone.flashbid.global.core.interceptor.ChatSubscribeInterceptor;

@EnableWebSocketMessageBroker
@EnableWebSocket
@Configuration
@Slf4j
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final ChatSubscribeInterceptor interceptor;
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        log.info("connect ws");
        registry.addEndpoint("/ws").setAllowedOriginPatterns("*");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(interceptor);
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 받는 쪽
        registry.enableSimpleBroker("/topic");
        // 보낼쪽
        registry.setApplicationDestinationPrefixes("/app");
    }
}
