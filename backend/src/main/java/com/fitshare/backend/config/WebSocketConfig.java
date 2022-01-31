package com.fitshare.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 클라이언트가 WebSocket Handshake로 커넥션을 생성할 HTTP URL endpoint
        registry.addEndpoint("/api/v1/chat")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 기본 Broker 사용
        // destination header가 /topic 으로 시작하는 메시지를 Broker로 라우트한다.
        // SUBSCRIBE frame일 때 사용된다
        registry.enableSimpleBroker("/topic");
        // destination header가 /app 으로 시작하는 메시지는 Controller의 @MessageMapping 메서드로 mapping
        // SEND frame일 때 사용된다
        registry.setApplicationDestinationPrefixes("/app");
    }

    // TODO: buffer size 설정
    // TODO: 클라이언트 연결이 끊겼을 때 재연결 코드 작성
}
