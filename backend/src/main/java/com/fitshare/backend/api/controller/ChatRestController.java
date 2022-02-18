package com.fitshare.backend.api.controller;


import com.fitshare.backend.api.request.FriendReq;
import com.fitshare.backend.api.service.ChatService;
import com.fitshare.backend.common.auth.JwtUtil;
import com.fitshare.backend.common.exception.handler.ErrorResponse;
import com.fitshare.backend.common.model.BaseResponseBody;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import static com.fitshare.backend.common.model.ResponseMessage.CHECK_CHAT;
import static com.fitshare.backend.common.model.ResponseMessage.GET_CHAT_LIST;

@Api(value = "채팅 REST API", tags = "Chat")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/chat-rest")
public class ChatRestController {

    private final ChatService chatService;

    @ApiOperation(value = "채팅 읽음 여부 업데이트", notes = "받은 메시지를 읽음처리하는 api입니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = GET_CHAT_LIST),
            @ApiResponse(code = 500, message = "서버 에러", response = ErrorResponse.class)
    })
    @PatchMapping(value = "/check")
    public ResponseEntity<BaseResponseBody> checkChat(@RequestBody FriendReq friendReq) {
        Long memberId = JwtUtil.getCurrentId().orElseThrow(() -> new AccessDeniedException("Access denied"));
        chatService.checkChatList(memberId, friendReq.getFriendId());
        return ResponseEntity.ok(BaseResponseBody.of(HttpStatus.OK, CHECK_CHAT));
    }

    @ApiOperation(value = "채팅 목록 요청", notes = "yyyyMMdd 형태의 날짜로 1:1 채팅 목록을 요청하는 api입니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = GET_CHAT_LIST),
            @ApiResponse(code = 500, message = "서버 에러", response = ErrorResponse.class)
    })
    @GetMapping(value = "/{friendId}/{date}")
    public ResponseEntity<BaseResponseBody> getPrivateChatListByDate(@PathVariable Long friendId, @PathVariable @DateTimeFormat(pattern = "yyyyMMdd") String date) {
        Long memberId = JwtUtil.getCurrentId().orElseThrow(() -> new AccessDeniedException("Access denied"));
        return ResponseEntity.ok(BaseResponseBody.of(HttpStatus.OK, GET_CHAT_LIST, chatService.getPrivateChatListByDate(memberId, friendId, date)));
    }

}
