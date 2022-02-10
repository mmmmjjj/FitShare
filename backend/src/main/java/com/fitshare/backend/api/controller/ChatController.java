package com.fitshare.backend.api.controller;

import com.fitshare.backend.api.request.ChatReq;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {
    private final SimpMessagingTemplate template; // Broker로 메세지를 전달

    @ApiOperation(value = "채팅", notes = "메시지 전송")
    @MessageMapping(value = "/chat/message")
    public void sendMessage(ChatReq chatReq) {
        template.convertAndSend("/topic/room/" + chatReq.getRoomId(), chatReq);
    }
}
