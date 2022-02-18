package com.fitshare.backend.config;

import com.fitshare.backend.common.auth.JwtTokenProvider;
import com.fitshare.backend.common.exception.JwtValidFailedException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@RequiredArgsConstructor
@EnableWebSocketMessageBroker
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final JwtTokenProvider tokenProvider;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 클라이언트가 WebSocket Handshake로 커넥션을 생성할 HTTP URL endpoint
        registry.addEndpoint("/api/v1/chat")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // destination header가 /queue, /topic 으로 시작하는 메시지를 기본 Broker로 routing
        // /queue: point-to-pint messaging, /topic: pub-sub messaging
        // SUBSCRIBE frame일 때 사용
        registry.enableSimpleBroker("/queue", "/topic");
        // destination header가 /app 으로 시작하는 메시지는 Controller의 @MessageMapping 메서드로 mapping
        // SEND frame일 때 사용
        registry.setApplicationDestinationPrefixes("/app");
        // client가 메시지를 순서대로 수신
        registry.setPreservePublishOrder(true);
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

                if (StompCommand.CONNECT.equals(accessor.getCommand()) ||
                        StompCommand.SUBSCRIBE.equals(accessor.getCommand()) ||
                        StompCommand.SEND.equals(accessor.getCommand())
                ) {
                    String token = accessor.getFirstNativeHeader("Authorization").substring(7);
                    if (StringUtils.hasText(token) && tokenProvider.validateToken(token)) {
                        Authentication authentication = tokenProvider.getAuthentication(token);
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        accessor.setUser(authentication);
                    } else {
                        throw new JwtValidFailedException("Token invalid");
                    }
                }

                return message;
            }
        });
    }
}
