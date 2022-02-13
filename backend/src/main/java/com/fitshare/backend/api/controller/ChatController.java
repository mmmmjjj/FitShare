package com.fitshare.backend.api.controller;

import com.fitshare.backend.api.request.ChatReq;
import com.fitshare.backend.api.request.PrivateChatReq;
import com.fitshare.backend.api.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;

    // 쇼핑룸 내 단체 메시지 전송
    @MessageMapping("/rooms/{roomId}")
    public ChatReq sendBroadcastMessage(ChatReq chatReq) {
        return chatReq;
    }

    // 개인 메시지 전송
    @MessageMapping("/messages")
    public void sendPrivateMessage(Principal principal, PrivateChatReq privateChatReq) {
        chatService.addPrivateChat(Long.valueOf(principal.getName()), privateChatReq);
        messagingTemplate.convertAndSendToUser(String.valueOf(privateChatReq.getReceiverId()), "/queue/messages", privateChatReq);
    }

}
