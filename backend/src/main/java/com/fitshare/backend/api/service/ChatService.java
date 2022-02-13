package com.fitshare.backend.api.service;

import com.fitshare.backend.api.request.PrivateChatReq;
import com.fitshare.backend.db.entity.PrivateChat;

import java.util.List;

public interface ChatService {

    void addPrivateChat(Long memberId, PrivateChatReq privateChatReq);

    void checkChat(Long senderId, Long receiverId);

    List<PrivateChat> getPrivateChatListByMembers(Long memberId, Long friendId);

}
