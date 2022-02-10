package com.fitshare.backend.config;

import com.fitshare.backend.common.auth.JwtTokenProvider;
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
        // 기본 Broker 사용
        // destination header가 /topic 으로 시작하는 메시지를 Broker로 라우트한다.
        // SUBSCRIBE frame일 때 사용된다
        registry.enableSimpleBroker("/topic");
        // destination header가 /app 으로 시작하는 메시지는 Controller의 @MessageMapping 메서드로 mapping
        // SEND frame일 때 사용된다
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

                System.out.println("command : " + accessor.getCommand());

                if (StompCommand.CONNECT.equals(accessor.getCommand()) ||
                        StompCommand.SUBSCRIBE.equals(accessor.getCommand()) ||
                        StompCommand.SEND.equals(accessor.getCommand()) ||
                        StompCommand.MESSAGE.equals(accessor.getCommand())
                ) {
                    String token = accessor.getFirstNativeHeader("Authorization").substring(7);
                    if (StringUtils.hasText(token) && tokenProvider.validateToken(token)) {
                        Authentication authentication = tokenProvider.getAuthentication(token);
                        SecurityContextHolder.getContext().setAuthentication(authentication);
//                        message = getNewMessage(message);
                    }
                }

                return message;
            }
        });
    }

//    private Message<?> getNewMessage(Message<?> message) {
//        Message newMessage = null;
//        String payloadString = new String((byte[]) message.getPayload());
//
//        if (payloadString.isEmpty()) {
//            return message;
//        }
//
//        Long id = JwtUtil.getCurrentId().orElse(null);
//
//        JSONParser jsonParser = new JSONParser();
//        JSONObject payloadObject = null;
//        try {
//            payloadObject = (JSONObject) jsonParser.parse(payloadString);
//            payloadString = payloadObject.toJSONString();
//            newMessage = MessageBuilder.createMessage(payloadString.getBytes(), message.getHeaders());
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        String newPayloadString = new String((byte[]) newMessage.getPayload());
//
//        return newMessage;
//    }

    // TODO: buffer size 설정
    // TODO: 클라이언트 연결이 끊겼을 때 재연결 코드 작성
}
