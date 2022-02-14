package com.fitshare.backend.listener;

import com.fitshare.backend.api.service.ChatService;
import com.fitshare.backend.api.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@RequiredArgsConstructor
@Component
public class ExpirationListener implements MessageListener {
    private final RedisService redisService;
    private final ChatService chatService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String key = message.toString();

        if (key.startsWith("shadowkey")) {
            key = key.substring(10);

            Map<String, String> entries = (Map<String, String>) redisService.getEntries(key);
            chatService.addPrivateChatListInMySql(chatService.getPrivateChatListFromEntries(key, entries));

            redisService.delData(key);
        }
    }
}
