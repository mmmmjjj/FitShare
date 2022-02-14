package com.fitshare.backend.listener;

import com.fitshare.backend.api.request.PrivateChatReq;
import com.fitshare.backend.api.service.ChatService;
import com.fitshare.backend.api.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
public class ExpirationListener implements MessageListener {
    private final RedisService redisService;
    private final ChatService chatService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String key = message.toString().substring(10);

        if (key.startsWith("shadowkey")) {
            String senderId = (String) redisService.getHashData(key, "sender_id");
            String receiverId = (String) redisService.getHashData(key, "receiver_id");
            String messageContents = (String) redisService.getHashData(key, "message");
            String createdTime = (String) redisService.getHashData(key, "created_time");

            PrivateChatReq privateChatReq = new PrivateChatReq();
            privateChatReq.setReceiverId(receiverId);
            privateChatReq.setMessage(messageContents);
            privateChatReq.setCreatedTime(LocalDateTime.parse(createdTime));

            chatService.addPrivateChatInMySql(Long.valueOf(senderId), privateChatReq);

            redisService.delData(key);
        }
    }
}
